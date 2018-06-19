import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scenario implements Constants {

    final private Map<String, Object> stepTree;

    final private Set<String> loopVarList;
    final private Set<String> envVarList;
    final private Set<String> allVarList;

    public Scenario(final Map<String, Object> tree) {
        stepTree = tree;

        loopVarList = new HashSet<>();
        envVarList = new HashSet<>();
        allVarList = new HashSet<>();

        extractVariables(stepTree);
        allVarList.addAll(envVarList);
        allVarList.addAll(loopVarList);
    }

    public static Map<String, Object> parseJson(final File scenarioSrcFile) throws IOException {
        return new ObjectMapper()
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
                .<Map<String, Object>>readValue(
                        scenarioSrcFile, new TypeReference<Map<String, Object>>() {
                        });
    }

    public Map<String, Object> getStepTree() {
        return stepTree;
    }

    public Set<String> getLoopVarList() {
        return loopVarList;
    }

    public Set<String> getEnvVarList() {
        return envVarList;
    }

    public Set<String> getAllVarList() {
        return allVarList;
    }

    private void extractEnvVariables(final String str) {
        final Pattern pattern = Pattern.compile(String.format(VAR_PATTERN, WITHOUT_SPACES_PATTERN));
        final Matcher matcher = pattern.matcher((String) str);
        while (matcher.find()) {
            String tmp = matcher.group(0).replaceAll("\\{|\\}|\\$", "");
            if (loopVarList.contains(tmp)) continue;
            envVarList.add(tmp);
        }
    }

    private void extractVariables(final Object o) {
        if (o instanceof Map)
            extractVariables((Map) o);
        else if (o instanceof List)
            extractVariables((List) o);
    }

    private void extractVariables(final List<Object> list) {
        for (Object item : list) {
            if (item instanceof Map)
                extractVariables((Map<String, Object>) item);
        }
    }

    private void extractVariables(final Map<String, Object> tree) {
        if (tree.containsValue(STEP_TYPE_FOR) & tree.containsKey(KEY_IN))
            loopVarList.add((String) tree.get(KEY_VALUE));
        else {
            for (String key : tree.keySet()) {
                if (tree.get(key) instanceof String)
                    extractEnvVariables((String) tree.get(key));
                extractVariables(tree.get(key));
            }
        }
        if (tree.containsKey(KEY_STEPS))
            extractVariables(tree.get(KEY_STEPS));

    }

}
