import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.print.DocFlavor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Converter {

    private int stepCounter = 0;
    private int cmdCounter = 0;

    private JSONScenario jsonScenario;
    private Set<String> loopVarList;
    private Set<String> envVarList;
    private Set<String> allVarList;

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

    private final String VAR_PATTERN = "\\$\\{%s\\}";
    private final String WITHOUT_SPACES_PATTERN = "([\\w\\-_.!@#%\\^&*=+()\\[\\]~:;'\\\\|/<>,?]+)";
    private final String QUOTES_PATTERN = "\"%s\"";
    private final String TAB = "    ";
    private final String INDEX_POSTFIX = "_i";
    private final String FOR_FORMAT = "for( var %s = 0; %s < %s.length; ++%s ){";
    private final String COMMAND_FORMAT = "%s var cmd_%d = new java.lang.ProcessBuilder()\n%s.command(\"sh\", \"-c\", %s)\n%s.start();";

    public Converter(Path oldScenarioPath) throws IOException {
        jsonScenario = new JSONScenario(oldScenarioPath.toFile());
        loopVarList = new HashSet<>();
        envVarList = new HashSet<>();
        envVarList.addAll(System.getenv().keySet());
        allVarList = new HashSet<>();
        extractVariables(jsonScenario.getStepTree());
        allVarList.addAll(envVarList);
        allVarList.addAll(loopVarList);
        replaceVariables(jsonScenario.getStepTree());
        System.out.println("loop Var-s : " + loopVarList);
        System.out.println("env  Var-s : " + envVarList);
        System.out.println("loop + env : " + allVarList);
    }

    private void replaceVariables(Map<String, Object> tree) {
        for (String key : tree.keySet()) {
            if (key.equals(KEY_TYPE) && tree.get(key).equals(STEP_TYPE_COMMAND))
                break;
            else if (tree.get(key) instanceof String) {
                for (String loopVar : allVarList)
                    tree.replace(key, ((String) tree.get(key)).replaceAll(String.format(VAR_PATTERN, loopVar), loopVar));
            } else replaceVariables(tree.get(key));
        }
    }

    private void replaceVariables(List<Object> list) {
        for (Object item : list) {
            if (item instanceof Map)
                replaceVariables((Map<String, Object>) item);
            else if (item instanceof String)
                for (String loopVar : allVarList)
                    item = ((String) item).replaceAll(String.format(VAR_PATTERN, loopVar), loopVar);
        }
    }

    private void replaceVariables(Object o) {
        if (o instanceof Map)
            replaceVariables((Map) o);
        else if (o instanceof List)
            replaceVariables((List) o);
    }

    private void extractEnvVariables(final String str) {
        Pattern pattern = Pattern.compile(String.format(VAR_PATTERN, WITHOUT_SPACES_PATTERN));
        Matcher matcher = pattern.matcher((String) str);
        while (matcher.find()) {
            String tmp = matcher.group(0).replaceAll("\\{|\\}|\\$", "");
            //if (allVarList.contains(tmp)) continue;
            System.out.println("!!!" + tmp);
            envVarList.add(tmp);
        }
    }

    private void extractVariables(Object o) {
        if (o instanceof Map)
            extractVariables((Map) o);
        else if (o instanceof List)
            extractVariables((List) o);
    }

    private void extractVariables(List<Object> list) {
        for (Object item : list) {
            if (item instanceof Map)
                extractVariables((Map<String, Object>) item);
        }
    }

    private void extractVariables(Map<String, Object> tree) {
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
        for (String var : loopVarList) {
            cmdLine = cmdLine.replaceAll(String.format(VAR_PATTERN, var), "\" + " + var + "_i + \"");
        }
        return String.format(COMMAND_FORMAT, tab, ++cmdCounter, tab, "\"" + cmdLine + "\"", tab);
    }

    private String createPrecondStep(String tab, Map<String, Object> config) {
        String str = tab + "var step_" + (++stepCounter) + " = PreconditionLoad.config("
                + mapToJSON(config) + ");\n";
        str += tab + "step_" + (stepCounter) + ".start();";
        return str;
    }

    private String createSeqVariable(final String varName, final List seq) {
        return "var " + varName + " = " + seq.toString() + ";";
    }

    public String createForStep(final String tab, final String varName, final List seq) {
        String str = tab + createSeqVariable(varName, seq) + "\n";
        String loopVar = varName + INDEX_POSTFIX;
        str += tab + String.format(FOR_FORMAT, loopVar, loopVar, varName, loopVar);
        return str;
    }

    public String createStepLoad(String tab, Map<String, Object> config) {
        String str = tab + "var step_" + (++stepCounter) + " = Load.config("
                + mapToJSON(config) + ");\n";
        str += tab + "step_" + (stepCounter) + ".start();";
        return str;
    }

    private String mapToJSON(Map map) {
        try {
            String str = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
            for (String var : allVarList)
                str = str.replaceAll(String.format(QUOTES_PATTERN, var), var);
            return str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new String();
    }
}