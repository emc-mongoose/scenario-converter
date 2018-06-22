import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.akurilov.commons.collection.TreeUtil;

import java.io.IOException;
import java.util.*;

public class ConfigConverter {

    public static String convertConfig(final String oldConfig) {
        String str = null;
        try {
            final Map<String, Object> map = new ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
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
                case Constants.KEY_STORAGE:
                case Constants.KEY_STEP: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                }
                break;
                case Constants.KEY_TEST: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_TEST)));
                }
                break;
                case Constants.KEY_LIMIT: {
                    addToSection((HashMap<String, Object>) tree.get(key), newTree,
                            new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_STEP, Constants.KEY_LIMIT)));
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_LIMIT)));
                }
                break;
                case Constants.KEY_SCENARIO: {
                    addToSection((Map<String, Object>) tree.get(key), newTree,
                            new ArrayList<>(Arrays.asList(Constants.KEY_RUN, Constants.KEY_SCENARIO)));
                }
                break;
                case Constants.KEY_RUN: {
                    addToSection((Map<String, Object>) tree.get(key), newTree,
                            new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_STEP)));
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_RUN)));
                }
                break;
                case Constants.KEY_NODE: {
                    addToSection((Map<String, Object>) tree.get(key), newTree,
                            new ArrayList<>(Arrays.asList(Constants.KEY_STORAGE, Constants.KEY_NET, Constants.KEY_NODE)));
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_STORAGE, Constants.KEY_NODE)));
                }
                break;
            }
        }
    }

    private static void addToSection(final Map o, final Map<String, Object> newTree, final List<String> keys) {
        if (!newTree.containsKey(keys.get(0))) {
            newTree.put(keys.get(0), new HashMap<String, Object>());
        }
        if (keys.size() == 1) {
            ((Map<String, Object>) newTree.get(keys.get(0))).putAll(o);
        } else {
            final String key = keys.remove(0);
            addToSection(o, (Map<String, Object>) newTree.get(key), keys);
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

    public static String convertConfigAndToJson(final Map<String, Object> oldConfig) {
        String str = null;
        try {
            str = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(convertConfig(oldConfig))
                    .replaceAll("\\\\\"", "\"");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
