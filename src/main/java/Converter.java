import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Converter {

    private int varCounter = 0;
    private int stepCounter = 0;
    private int cmdCounter = 0;
    private int forCounter = 0;

    private JSONScenario jsonScenario;
    private List<String> loopVarList;

    private final String STEP_TYPE_FOR = "for";
    private final String STEP_TYPE_COMMAND = "command";
    private final String STEP_TYPE_SEQ = "sequential";
    private final String STEP_TYPE_LOAD = "load";
    private final String STEP_TYPE_PARALLEL = "parallel";
    private final String STEP_TYPE_PRECONDITION = "precondition";

    private final String KEY_STEPS = "steps";
    private final String KEY_TYPE = "type";
    private final String KEY_CONFIG = "config";
    private final String KEY_VALUE = "value";
    private final String KEY_IN = "in";

    private final String TAB = "    ";

    public Converter(Path oldScenarioPath) throws IOException {
        jsonScenario = new JSONScenario(oldScenarioPath.toFile());
        loopVarList = new ArrayList<>();
        extractVariables(jsonScenario.getStepTree());
        replaceVariables(jsonScenario.getStepTree());
        System.out.println(loopVarList);
    }

    private void replaceVariables(Map<String, Object> tree) {
        for (String key : tree.keySet()) {
            if (tree.get(key) instanceof String) {
                for (String loopVar : loopVarList)
                    tree.replace(key, ((String) tree.get(key)).replaceAll("\\$\\{" + loopVar + "\\}", loopVar));
            } else replaceVariables(tree.get(key));
        }
    }

    private void replaceVariables(List<Object> list) {
        for (Object item : list) {
            if (item instanceof Map)
                replaceVariables((Map<String, Object>) item);
            if (item instanceof String)
                for (String loopVar : loopVarList)
                    item = ((String) item).replaceAll("\\$\\{" + loopVar + "\\}", loopVar);
        }
    }

    private void replaceVariables(Object o) {
        if (o instanceof Map)
            replaceVariables((Map) o);
        if (o instanceof List)
            replaceVariables((List) o);
    }

    private void extractVariables(Object o) {
        if (o instanceof Map)
            extractVariables((Map) o);
        if (o instanceof List)
            extractVariables((List) o);
    }

    private void extractVariables(List<Object> list) {
        for (Object item : list) {
            Map<String, Object> tree = (Map<String, Object>) item;
            extractVariables(tree);
        }
    }

    private void extractVariables(Map<String, Object> tree) {
        if (tree.containsValue(STEP_TYPE_FOR))
            loopVarList.add((String) tree.get(KEY_VALUE));
        if (tree.containsKey(KEY_STEPS))
            extractVariables(tree.get(KEY_STEPS));
    }

    public void print() {
        print("", jsonScenario.getStepTree());
    }

    private void print(final String tab, final Map<String, Object> tree) {
        if (tree.containsKey(KEY_TYPE)) {
            String key = KEY_TYPE;
            switch ((String) tree.get(key)) {
                case STEP_TYPE_COMMAND: {
                    String str = createCommandStep(tab, (String) tree.get(KEY_VALUE));
                    System.out.print("\n" + str + "\n");
                }
                break;
                case STEP_TYPE_SEQ: {
                    print(tab + TAB, (ArrayList<Object>) tree.get(KEY_STEPS));
                }
                break;
                case STEP_TYPE_PARALLEL: {
                    System.out.println("\n<PARALLEL> : {");
                    print(tab + TAB, (ArrayList<Object>) tree.get(KEY_STEPS));
                    System.out.println(tab + "}");
                }
                break;
                case STEP_TYPE_PRECONDITION: {
                    String str = createPrecondStep(tab, (Map<String, Object>) tree.get(KEY_CONFIG));
                    System.out.print("\n" + str + "\n");
                }
                break;
                case STEP_TYPE_FOR: {

                    final List inValue = (List) tree.get(KEY_IN);
                    final String varName = (String) tree.get(KEY_VALUE);
                    if (inValue != null) {
                        String str = createForStep(tab, varName, inValue);
                        System.out.print("\n" + str + "\n");
                    }

                    print(tab + TAB, (ArrayList<Object>) tree.get(KEY_STEPS));
                    System.out.println(tab + "}");
                }
                break;
                case STEP_TYPE_LOAD: {
                    String str = createStepLoad(tab, (Map<String, Object>) tree.get(KEY_CONFIG));
                    System.out.print("\n" + str + "\n");
                }
                break;
                default:
                    System.out.println(tab + "<" + tree.get(key) + ">");
            }
        }
    }

    private void print(final String tab, final ArrayList<Object> steps) {
        for (Object step : steps) {
            if (step instanceof Map) {
                print(tab + " ", (Map<String, Object>) step);
            }
        }
    }

    public String createCommandStep(String tab, String cmdLine) {
        return tab + "var cmd_" + (++cmdCounter) + " = new java.lang.ProcessBuilder()\n" +
                tab + ".command(\"sh\", \"-c\", " + cmdLine + ")\n" +
                tab + ".start();";

    }

    private String createPrecondStep(String tab, Map<String, Object> config) {
        String str = tab + "var step_" + (++stepCounter) + " = PreconditionLoad.config( { "
                + mapToJSON(config) + "});\n";
        str += tab + "step_" + (stepCounter) + ".start();";
        return str;
    }

    private String createSeqVariable(final String varName, final List seq) {
        return "var " + varName + " = " + seq.toString() + ";";
    }

    public String createForStep(final String tab, final String varName, final List seq) {
        String str = tab + createSeqVariable(varName, seq) + "\n";
        str += tab + "for( var i_" + (++forCounter) + " = 0; i_" + forCounter + " < " + varName
                + ".length; ++i_" + forCounter + "){";
        return str;
    }

    public String createStepLoad(String tab, Map<String, Object> config) {
        String str = tab + "var step_" + (++stepCounter) + " = Load.config( { "
                + mapToJSON(config) + "});\n";
        str += tab + "step_" + (stepCounter) + ".start();";
        return str;
    }

    private String mapToJSON(Map map){
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new String();
    }
}