import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigConverter implements Constants {

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
        final Map<String, Object> newConfig = oldConfig;
        convert(oldConfig, newConfig);
        return newConfig;
    }

    private static void convert(final Map<String, Object> tree, final Map<String, Object> newTree) {
        for (String key : tree.keySet()) {
            switch (key) {
                case KEY_LOAD: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                }
                break;
                case KEY_LIMIT: {
                    addToLoadSection(tree.get(key), newTree);
                    tree.remove(KEY_LIMIT);
                }
                break;
                case KEY_TEST: {
                    convert((Map<String, Object>) tree.get(key), newTree);
                    tree.remove(KEY_TEST);
                }
                break;
                case KEY_STEP: {
                    addToLoadSection(tree.get(key), newTree);
                    tree.remove(KEY_STEP);
                }
                break;
                case KEY_SCENARIO: {
                    addToRunSection(tree.get(key), newTree);
                    tree.remove(KEY_SCENARIO);
                }
                break;
            }
        }
    }

    private static void addToRunSection(final Object o, final Map<String, Object> newTree) {
        if (!newTree.containsKey(KEY_RUN)) {
            newTree.put(KEY_RUN, new HashMap<>());
        }
        ((Map<String, Object>) newTree.get(KEY_RUN)).put(KEY_SCENARIO, o);
    }

    private static void addToLoadSection(final Object o, final Map<String, Object> newTree) {
        if (!newTree.containsKey(KEY_LOAD)) {
            newTree.put(KEY_LOAD, new HashMap<>());
        }
        ((Map<String, Object>) newTree.get(KEY_LOAD)).put(KEY_STEP, o);
    }

    public static String convertConfigAndToJson(final Map<String, Object> oldConfig) {
        String str = null;
        try {
            str = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(convertConfig(oldConfig));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
