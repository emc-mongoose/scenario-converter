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
                } break;
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

    private String createPrecondStep(String tab, Map<String,Object> config) {
        String str = tab + "var step_" + (++stepCounter) + " = PreconditionLoad.config( { "
                + config + "});\n";
        str += tab + "step_" + (stepCounter) + ".start();";
        return str;
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
                + config + "});\n";
        str += tab + "step_" + (stepCounter) + ".start();";
        return str;
    }

//    private void loadDefaultConfig() throws IOException {
//        final String defaultConfigPath = PATH_DEFAULTS;
//        new File(defaultConfigPath);
//
//        final ObjectMapper mapper = new ObjectMapper()
//                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
//                .configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
//        config = mapper.readValue(new File(defaultConfigPath), Config.class);
//    }
//
//    public void print() {
//        print(jsonScenario.getChildSteps(), "");
//    }
//
//    private void print(List<Step> steps, String tab) {
//        for (Step step : steps) {
//            //System.out.print("\n" + tab + step.getClass().getSimpleName().toString());
//            //System.out.print(" {");
//            switch (step.getClass().getSimpleName()) {
//                case STEP_TYPE_FOR: {
//                    Object fieldValue = getFieldValue("valueSeq", step, ForStep.class);
//                    String str = createForStep(tab, (List) fieldValue);
//                    System.out.print("\n\n" + str + "\n");
//                    Pattern pattern = Pattern.compile("type=[a-z]*");
//                    Matcher matcher = pattern.matcher(((ForStep) step).stepsTreeList.toString());
//                    while (matcher.find()) {
//                        String childsteps = matcher.group(0);
//                        System.out.println("<" + childsteps.split("=")[1] + ">");
//                    }
//                    for(Object o : ((ForStep) step).rawChildSteps) {
//                        System.out.println(o);
//                    }
//
//                }
//                break;
//                case STEP_TYPE_COMMAND: {
//                    Object fieldValue = getFieldValue("cmdLine", step, CommandStep.class);
//                    String str = createCommandStep(tab, (String) fieldValue);
//                    System.out.print("\n\n" + str + "\n");
//                }
//                break;
//                case STEP_TYPE_LOAD: {
//                    String str = createStepLoad(tab, step);
//                    System.out.print("\n\n" + str + "\n");
//                }
//                break;
//                case STEP_TYPE_PARALLEL: {
//                    String str = createParallelStep(tab);
//                    System.out.print("\n\n" + str + "\n");
//                }
//                break;
//            }
//
//            if (step instanceof CompositeStepBase && !(step instanceof ForStep))
//                print(((CompositeStepBase) step).getChildSteps(), tab + "  ");
//
//            if (step instanceof CompositeStepBase && (step instanceof ForStep))
//                print(((CompositeStepBase) step).getChildSteps(), tab + "  ");
//
//            //System.out.print("\n" + tab + "}");
//        }
//    }
//

//
//    public String varDefineLine(final Object variable) {
//        if (variable instanceof List) return createSeqVariable((List) variable);
//        if (variable instanceof String) return createStrVariable((String) variable);
//        return null;
//    }
//
//    private String createStrVariable(final String fieldValue) {
//        return "var str_" + (++varCounter) + " = \"" + fieldValue.toString() + "\";";
//    }
//
//    private String createSeqVariable(final List fieldValue) {
//        return "var seq_" + (++varCounter) + " = " + fieldValue.toString() + ";";
//    }

//    public String createForStep(String tab, List seq) {
//        //for(var size = 0; size < itemDataSizes.length; ++size) {
//
//        String str = tab + createSeqVariable(seq) + "\n";
//        str += tab + "for( var i_" + (++forCounter) + " = 0; i_" + forCounter + " < seq_" + varCounter
//                + ".length; ++i_" + forCounter + "){}";
//        return str;
//    }
//
//    public String createParallelStep(String tab) {
//        return tab + "Parallel step {}";
//    }

}
