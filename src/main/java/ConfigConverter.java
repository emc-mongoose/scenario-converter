import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigConverter implements IConverter {

    static private Map<String, Object> tree;

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
        tree = oldConfig;
        convert(tree);
        return tree;
    }

    private static void convert(Map<String, Object> tree) {
        for (String key : tree.keySet()) {
            switch (key) {
                case KEY_LOAD: {
                    convert((Map<String, Object>) tree.get(key));
                }
                break;
                case KEY_LIMIT: {
                    addToLoadSection(tree.get(key));
                    tree.remove(KEY_LIMIT);
                }
                break;
                case KEY_TEST: {
                    convert((Map<String, Object>) tree.get(key));
                    tree.remove(KEY_TEST);
                }
                break;
                case KEY_STEP: {
                    addToLoadSection(tree.get(key));
                    tree.remove(KEY_STEP);
                }
                break;
                case KEY_SCENARIO: {
                    addToRunSection(tree.get(key));
                    tree.remove(KEY_SCENARIO);
                }
                break;
            }
        }
    }

    private static void addToRunSection(Object o) {
        if (!tree.containsKey(KEY_RUN))
            tree.put(KEY_RUN, new HashMap<>());
        ((Map<String, Object>) tree.get(KEY_RUN)).put(KEY_SCENARIO, o);
        Logger.getLogger(ConfigConverter.class.getName()).info("Insert into RunSection : o = [" + o + "]");
    }

    private static void addToLoadSection(Object o) {
        if (!tree.containsKey(KEY_LOAD))
            tree.put(KEY_LOAD, new HashMap<>());
        ((Map<String, Object>) tree.get(KEY_LOAD)).put(KEY_STEP, o);
        Logger.getLogger(ConfigConverter.class.getName()).info("Insert into LoadSection : o = [" + o + "]");
    }


    private static List convert(List list) {
        return new ArrayList();
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
