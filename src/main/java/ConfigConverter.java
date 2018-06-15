import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ConfigConverter {
    public static String convertConfig(final String oldConfig) {
        String str = null;
        try {
            Map<String, Object> map = new ObjectMapper()
                    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
                    .<Map<String, Object>>readValue(
                            oldConfig, new TypeReference<Map<String, Object>>() {
                            }
                    );
            str = convertConfigAndToJSON(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Map<String, Object> convertConfig(final Map<String, Object> oldConfig) {
        return oldConfig;
    }

    public static String convertConfigAndToJSON(final Map<String, Object> oldConfig) {
        String str = null;
        try {
            str = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(convertConfig(oldConfig));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
