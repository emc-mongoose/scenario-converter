import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigConverter implements IConverter {

    static private Map<String, Object> tree;
    static private Map<String, Object> emptyLoadStepTree = new HashMap<String, Object>();

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
            if (key.equals(KEY_TEST)) {
                convert((Map<String, Object>) tree.get(key));
                tree.remove(KEY_TEST);
            } else if (key.equals(KEY_STEP)) {
                addToLoadSection(tree.get(key));
                tree.remove(KEY_STEP);
            } else if (key.equals(KEY_RUN)) {
                addToRunSection(tree.get(key));
                tree.remove(KEY_RUN);
            }
        }
    }

    private static void addToRunSection(Object o) {
        System.out.println("Insert to RunSection : o = [" + o + "]");
    }

    private static void addToLoadSection(Object o) {
        if (!tree.containsKey(KEY_LOAD)) {
            emptyLoadStepTree.put(KEY_STEP, o);
            tree.put(KEY_LOAD, emptyLoadStepTree);
            System.out.println("Insert ti LoadSection : o = [" + o + "]");
        } else
            for (String key : tree.keySet()) {
                if (key.equals(KEY_LOAD)) {
                    System.out.println("Insert ti LoadSection : o = [" + o + "]");
                }
            }
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
