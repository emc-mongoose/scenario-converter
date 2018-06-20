import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.akurilov.commons.collection.TreeUtil;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class ConfigConverter implements Constants {

    public static String convertConfig(final String oldConfig) {
        String str = null;
        try {
            final Map<String, Object> map = new ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                    .<Map<String, Object>>readValue(
                            oldConfig, new TypeReference<Map<String, Object>>() {
                            }
                    );
            str = convertConfigAndToJson(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Map<String, Object> convertConfig(final Map<String, Object> oldConfig) {
        final Map<String, Object> newConfig = TreeUtil.copyTree(oldConfig);
        convert(oldConfig, newConfig);
        return newConfig;
    }

    public static String pullLoadType(final Map<String, Object> config) {
        if (((Map<String, Object>) config).containsKey(KEY_LOAD) &&
                ((Map<String, Object>) ((Map<String, Object>) config).get(KEY_LOAD)).containsKey(KEY_TYPE))
            return (String) ((Map<String, Object>) config.get(KEY_LOAD)).get(KEY_TYPE);
//        if (type != null) {
//            ((Map<String, Object>) config.get(KEY_LOAD)).remove(KEY_TYPE);
//            if (((Map<String, Object>) config.get(KEY_LOAD)).isEmpty()) {
//                config.remove(KEY_LOAD);
//            }
//        }
        return KEY_CREATE;
    }

    private static void convert(final Map<String, Object> tree, final Map<String, Object> newTree) {
        for (String key : tree.keySet()) {
            switch (key) {
                case KEY_LOAD:
                case KEY_STORAGE: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                }
                break;
                case KEY_LIMIT: {
                    final Map map = new HashMap<String, Object>();
                    map.put(KEY_LIMIT, tree.get(key));
                    addToLoadStepSection(map, newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(KEY_LOAD, KEY_LIMIT)));
                    //newTree.remove(KEY_LIMIT);
                }
                break;
                case KEY_TEST: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(KEY_TEST)));
                    //newTree.remove(KEY_TEST);
                }
                break;
                case KEY_STEP: {
                    addToLoadStepSection(tree.get(key), newTree);
                    //deepRemove(newTree, Arrays.asList(KEY_TEST, KEY_STEP));
                    //newTree.remove(KEY_STEP);
                }
                break;
                case KEY_SCENARIO: {
                    addToRunSection(tree.get(key), newTree);
                    //deepRemove(newTree, Arrays.asList(KEY_TEST, KEY_SCENARIO));
                    //newTree.remove(KEY_SCENARIO);
                }
                break;
                case KEY_RUN: {
                    addToLoadStepSection(tree.get(key), newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(KEY_RUN)));
                }
                break;
                case KEY_NODE: {
                    final Map map = new HashMap<String, Object>();
                    map.put(KEY_NODE, tree.get(key));
                    addToStorageNetSection(map, newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(KEY_STORAGE, KEY_NODE)));
                }
                break;
            }
        }
    }

    private static void addToStorageNetSection(final Object o, final Map<String, Object> newTree) {
        if (!((Map<String, Object>) newTree.get(KEY_STORAGE)).containsKey(KEY_NET)) {
            ((Map<String, Object>) newTree
                    .get(KEY_STORAGE))
                    .put(KEY_NET, o);
        } else {
            ((Map<String, Object>) ((Map<String, Object>) newTree
                    .get(KEY_STORAGE))
                    .get(KEY_NET))
                    .putAll((HashMap<String, Object>) o);
        }
    }

    private static void deepRemove(Map<String, Object> tree, final List<String> keys) {
        if (keys.size() == 1)
            tree.remove(keys.get(0));
        else {
            final String key = keys.remove(0);
            deepRemove((Map<String, Object>) tree.get(key), keys);
        }
    }

    private static void addToRunSection(final Object o, final Map<String, Object> newTree) {
        if (!newTree.containsKey(KEY_RUN)) {
            newTree.put(KEY_RUN, new HashMap<>());
        }
        ((Map<String, Object>) newTree.get(KEY_RUN)).put(KEY_SCENARIO, o);
    }

    private static void addToLoadStepSection(final Object o, final Map<String, Object> newTree) {
        if (!newTree.containsKey(KEY_LOAD)) {
            final Map<String, Object> branch = new HashMap<>();
            branch.put(KEY_STEP, o);
            newTree.put(KEY_LOAD, branch);
        } else if (!((Map<String, Object>) newTree.get(KEY_LOAD)).containsKey(KEY_STEP)) {
            ((Map<String, Object>) newTree
                    .get(KEY_LOAD))
                    .put(KEY_STEP, o);
        } else {
            ((Map<String, Object>) ((Map<String, Object>) newTree
                    .get(KEY_LOAD))
                    .get(KEY_STEP))
                    .putAll((HashMap<String, Object>) o);
        }
    }

    public static String convertConfigAndToJson(final Map<String, Object> oldConfig) {
        String str = null;
        try {
            str = new ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(convertConfig(oldConfig));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
