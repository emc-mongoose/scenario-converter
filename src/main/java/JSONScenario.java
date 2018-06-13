import com.emc.mongoose.ui.log.Loggers;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JSONScenario {

    private static final Pattern PATTERN_ENV_VAR = Pattern.compile(
            "\\$\\{([\\w\\-_.!@#%\\^&*=+()\\[\\]~:;'\\\\|/<>,?]+)\\}"
    );

    private Map<String, Object> stepTree;

    public JSONScenario(final File scenarioSrcFile) throws IOException {
        this(
                new ObjectMapper()
                        .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                        .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
                        .<Map<String, Object>>readValue(
                                scenarioSrcFile, new TypeReference<Map<String, Object>>() {
                                }
                        )
        );
    }

    public JSONScenario(final Map<String, Object> tree) {
        stepTree = overrideByEnv(validateAgainstSchema(tree));
    }

    public Map<String, Object> getStepTree() {
        return stepTree;
    }

    private static Map<String, Object> validateAgainstSchema(final Map<String, Object> tree) {
        return tree;
    }

    private static Map<String, Object> overrideByEnv(final Map<String, Object> tree) {

        Object value;
        String valueStr;
        Matcher m;
        String propertyName;
        String newValue;
        boolean alteredFlag;

        for (final String key : tree.keySet()) {
            value = tree.get(key);
            if (value instanceof Map) {
                overrideByEnv((Map<String, Object>) value);
            } else if (value instanceof List) {
                overrideByEnv((List<Object>) value);
            } else if (value instanceof String) {
                valueStr = (String) value;
                m = PATTERN_ENV_VAR.matcher(valueStr);
                alteredFlag = false;
                while (m.find()) {
                    propertyName = m.group(1);
                    if (propertyName != null && !propertyName.isEmpty()) {
                        newValue = System.getenv(propertyName);
                        if (newValue != null) {
                            valueStr = valueStr.replace("${" + propertyName + "}", newValue);
                            alteredFlag = true;
                            Loggers.MSG.info(
                                    "Key \"{}\": replaced \"{}\" with new value \"{}\"",
                                    key, propertyName, newValue
                            );
                        }
                    }
                }
                if (alteredFlag) {
                    tree.put(key, valueStr);
                }
            }
        }

        return tree;
    }

    private static List<Object> overrideByEnv(final List<Object> values) {

        Object value;
        String valueStr;
        Matcher m;
        String propertyName;
        String newValue;
        boolean alteredFlag;

        for (int i = 0; i < values.size(); i++) {
            value = values.get(i);
            if (value instanceof Map) {
                overrideByEnv((Map<String, Object>) value);
            } else if (value instanceof List) {
                overrideByEnv((List) value);
            } else if (value instanceof String) {
                valueStr = (String) value;
                m = PATTERN_ENV_VAR.matcher(valueStr);
                alteredFlag = false;
                while (m.find()) {
                    propertyName = m.group(1);
                    if (propertyName != null && !propertyName.isEmpty()) {
                        newValue = System.getenv(propertyName);
                        if (newValue != null) {
                            valueStr = valueStr.replace("${" + propertyName + "}", newValue);
                            alteredFlag = true;
                            Loggers.MSG.info(
                                    "Value #{}: replaced \"{}\" with new value \"{}\"",
                                    i, propertyName, newValue
                            );
                        }
                    }
                }
                if (alteredFlag) {
                    values.set(i, valueStr);
                }
            }
        }
        return values;
    }

}
