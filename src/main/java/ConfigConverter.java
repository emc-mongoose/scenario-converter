import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.akurilov.commons.collection.TreeUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigConverter {

    public static String convertConfig(final String oldConfig) {
        String str = null;
        try {
            final Map<String, Object> map = new ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
                    .readValue(
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

    private static String replaceArrays(final String str) {
        final Pattern p = Pattern.compile(":\\s*\\[[^\\]]*\\]");
        final Matcher m = p.matcher(str);
        String result = str;
        while (m.find()) {
            final String entry = m.group(0).split("\\[|\\]")[1];
            result = str.replaceAll("\\[" + entry + "\\]", "new java.util.ArrayList([" + entry + "])");
        }
        return result;
    }

    public static String pullLoadType(final Map<String, Object> config) {
        String type = new String();
        if (config.containsKey(Constants.KEY_LOAD))
            if (((Map<String, Object>) config.get(Constants.KEY_LOAD)).containsKey(Constants.KEY_TYPE)) {
                type = (String) ((Map<String, Object>) config.get(Constants.KEY_LOAD)).get(Constants.KEY_TYPE);
            }
        if (type.isEmpty()) {
            ((Map<String, Object>) config.get(Constants.KEY_LOAD)).remove(Constants.KEY_TYPE);
            if (((Map<String, Object>) config.get(Constants.KEY_LOAD)).isEmpty()) {
                config.remove(Constants.KEY_LOAD);
            }
        }
        return type;
    }

    private static void convert(final Map<String, Object> tree, final Map<String, Object> newTree) {
        for (String key : tree.keySet()) {
            switch (key) {
                case Constants.KEY_LOAD:
                case Constants.KEY_STORAGE:
                case Constants.KEY_STEP:
                case Constants.KEY_AUTH: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                }
                break;
                case Constants.KEY_ID: {
                    final Map<String, Object> uid = new HashMap<>();
                    uid.put(Constants.KEY_UID, tree.get(key));
                    addToSection(uid, newTree,
                            new ArrayList<>(Arrays.asList(Constants.KEY_STORAGE, Constants.KEY_AUTH)));
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_STORAGE, Constants.KEY_AUTH, Constants.KEY_ID)));
                }
                break;
                case Constants.KEY_TEST: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_TEST)));
                }
                break;
                case Constants.KEY_THREADS: {
                    final Map<String, Object> conc = new HashMap<>();
                    conc.put(Constants.KEY_CONCURRENCY, tree.get(key));
                    addToSection(conc, newTree,
                            new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_STEP, Constants.KEY_LIMIT)));
                    deepRemove(newTree, new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_THREADS)));
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
        return mapToStr(convertConfig(oldConfig));
    }

    public static String mapToStr(final Map<String, Object> map) {
        String str = null;
        try {
            str = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(map)
                    .replaceAll("\\\\\"", "\"");
            str = replaceArrays(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Map addWeight(final Map config, final Object weight) {
        final Map newConfig = config;
        final Map w = new HashMap();
        w.put(Constants.KEY_WEIGHT, weight);
        addToSection(w, newConfig, new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_GENERATOR)));
        return newConfig;
    }

    public static Map pullLoadStepSection(final Map config) {
        final Map section = new HashMap();
        if (config.containsKey(Constants.KEY_LOAD)) {
            final Map load = ((Map) config.get(Constants.KEY_LOAD));
            if (load.containsKey(Constants.KEY_STEP)) {
                final Object step = load.get(Constants.KEY_STEP);
                section.put(Constants.KEY_LOAD, new HashMap<>());
                ((Map) section.get(Constants.KEY_LOAD)).put(Constants.KEY_STEP, step);
                deepRemove(config, new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_STEP)));
            }
        }
        return section;
    }

    public static String pullLoadStepSectionStr(final Map config) {
        return mapToStr(pullLoadStepSection(config));
    }

}
