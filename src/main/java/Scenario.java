import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scenario {

    private final Map<String, Object> stepTree;
    private final Set<String> loopVarList;
    private final Set<String> envVarList;
    private final Set<String> allVarList;

    public Scenario(final Map<String, Object> tree) {
        stepTree = tree;

        loopVarList = new HashSet<>();
        envVarList = new HashSet<>();
        allVarList = new HashSet<>();

        extractLoopVariables(stepTree);
        extractEnvVariables(stepTree);
        allVarList.addAll(envVarList);
        allVarList.addAll(loopVarList);
    }

    public static Map<String, Object> parseJson(final File scenarioSrcFile) throws IOException {
        String lines = new String(Files.readAllBytes(scenarioSrcFile.toPath()));
        lines = lines.replaceAll("\\\\", "\\\\\\\\\\\\");
        return new ObjectMapper()
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
                .<Map<String, Object>>readValue(
                        lines, new TypeReference<Map<String, Object>>() {
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
        final Pattern pattern = Pattern.compile(String.format(Constants.VAR_PATTERN, Constants.WITHOUT_SPACES_PATTERN));
        final Matcher matcher = pattern.matcher((String) str);
        while (matcher.find()) {
            String tmp = matcher.group(0).replaceAll("\\{|\\}|\\$", "");
            if (loopVarList.contains(tmp)) continue;
            envVarList.add(tmp);
        }
    }

    private void extractEnvVariables(final Object tree) {
        if (tree instanceof List) {
            for (Object item : (List) tree) {
                extractEnvVariables(item);
            }
        } else {
            if (tree instanceof Map) {
                for (Object key : ((Map) tree).keySet()) {
                    if (((Map) tree).get(key) instanceof String)
                        extractEnvVariables((String) ((Map) tree).get(key));
                    else
                        extractEnvVariables(((Map) tree).get(key));
                }
            }
        }
    }

    private void extractLoopVariables(final Object tree) {
        if (tree instanceof List) {
            for (Object item : (List) tree) {
                extractLoopVariables(item);
            }
        } else {
            if (tree instanceof Map) {
                if (((Map) tree).containsValue(Constants.STEP_TYPE_FOR) & ((Map) tree).containsKey(Constants.KEY_IN))
                    loopVarList.add((String) ((Map) tree).get(Constants.KEY_VALUE));
                else {
                    for (Object key : ((Map) tree).keySet()) {
                        extractLoopVariables(((Map) tree).get(key));
                    }
                }
                if (((Map) tree).containsKey(Constants.KEY_STEPS))
                    extractLoopVariables(((Map) tree).get(Constants.KEY_STEPS));
            }
        }
    }

}
