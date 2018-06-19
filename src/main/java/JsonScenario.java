import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

class JsonScenario {

    private Map<String, Object> stepTree;

    public JsonScenario(final File scenarioSrcFile) throws IOException {
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

    public JsonScenario(final Map<String, Object> tree) {
        stepTree = tree;
    }

    public Map<String, Object> getStepTree() {
        return stepTree;
    }

}
