import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.akurilov.commons.collection.TreeUtil;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class ConfigConverter {

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
        if (((Map<String, Object>) config).containsKey(Constants.KEY_LOAD) &&
                ((Map<String, Object>) ((Map<String, Object>) config).get(Constants.KEY_LOAD)).containsKey(Constants.KEY_TYPE))
            return (String) ((Map<String, Object>) config.get(Constants.KEY_LOAD)).get(Constants.KEY_TYPE);
//        if (type != null) {
//            ((Map<String, Object>) config.get(KEY_LOAD)).remove(KEY_TYPE);
//            if (((Map<String, Object>) config.get(KEY_LOAD)).isEmpty()) {
//                config.remove(KEY_LOAD);
//            }
//        }
        return Constants.KEY_CREATE;
    }

    private static void convert(final Map<String, Object> tree, final Map<String, Object> newTree) {
        for (String key : tree.keySet()) {
            switch (key) {
                case Constants.KEY_LOAD:
                case Constants.KEY_STORAGE: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                }
                break;
                case Constants.KEY_LIMIT: {
                    final Map map = new HashMap<String, Object>();
                    map.put(Constants.KEY_LIMIT, tree.get(key));
                    addToLoadStepSection(map, newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_LIMIT)));
                    //newTree.remove(KEY_LIMIT);
                }
                break;
                case Constants.KEY_TEST: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_TEST)));
                    //newTree.remove(KEY_TEST);
                }
                break;
                case Constants.KEY_STEP: {
                    addToLoadStepSection(tree.get(key), newTree);
                    //deepRemove(newTree, Arrays.asList(KEY_TEST, KEY_STEP));
                    //newTree.remove(KEY_STEP);
                }
                break;
                case Constants.KEY_SCENARIO: {
                    addToRunSection(tree.get(key), newTree);
                    //deepRemove(newTree, Arrays.asList(KEY_TEST, KEY_SCENARIO));
                    //newTree.remove(KEY_SCENARIO);
                }
                break;
                case Constants.KEY_RUN: {
                    addToLoadStepSection(tree.get(key), newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_RUN)));
                }
                break;
                case Constants.KEY_NODE: {
                    final Map map = new HashMap<String, Object>();
                    map.put(Constants.KEY_NODE, tree.get(key));
                    addToStorageNetSection(map, newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_STORAGE, Constants.KEY_NODE)));
                }
                break;
            }
        }
    }

    private static void addToStorageNetSection(final Object o, final Map<String, Object> newTree) {
        if (!((Map<String, Object>) newTree.get(Constants.KEY_STORAGE)).containsKey(Constants.KEY_NET)) {
            ((Map<String, Object>) newTree
                    .get(Constants.KEY_STORAGE))
                    .put(Constants.KEY_NET, o);
        } else {
            ((Map<String, Object>) ((Map<String, Object>) newTree
                    .get(Constants.KEY_STORAGE))
                    .get(Constants.KEY_NET))
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
        if (!newTree.containsKey(Constants.KEY_RUN)) {
            newTree.put(Constants.KEY_RUN, new HashMap<>());
        }
        ((Map<String, Object>) newTree.get(Constants.KEY_RUN)).put(Constants.KEY_SCENARIO, o);
    }

    private static void addToLoadStepSection(final Object o, final Map<String, Object> newTree) {
        if (!newTree.containsKey(Constants.KEY_LOAD)) {
            final Map<String, Object> branch = new HashMap<>();
            branch.put(Constants.KEY_STEP, o);
            newTree.put(Constants.KEY_LOAD, branch);
        } else if (!((Map<String, Object>) newTree.get(Constants.KEY_LOAD)).containsKey(Constants.KEY_STEP)) {
            ((Map<String, Object>) newTree
                    .get(Constants.KEY_LOAD))
                    .put(Constants.KEY_STEP, o);
        } else {
            ((Map<String, Object>) ((Map<String, Object>) newTree
                    .get(Constants.KEY_LOAD))
                    .get(Constants.KEY_STEP))
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
