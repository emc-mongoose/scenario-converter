import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class ScenarioConverter implements Constants {

    final private static AtomicInteger stepCounter = new AtomicInteger(0);
    final private static AtomicInteger cmdCounter = new AtomicInteger(0);
    final private static AtomicInteger forCounter = new AtomicInteger(0);
    final private static AtomicInteger parallelCounter = new AtomicInteger(0);
    final private static AtomicInteger superConfigCounter = new AtomicInteger(0);

    private static Scenario oldScenario;

    public static void print(final Scenario scenario) throws IOException {
        oldScenario = scenario;
        final Map<String, Object> tree = oldScenario.getStepTree();
        replaceVariables(tree);
        //replaceJobsOnSteps(tree);
        print("", tree, false, new ArrayList<>());
    }

    private static void replaceJobsOnSteps(final Map<String, Object> tree) {
        //tree.forEach();
    }

    private static void print(final String tab, final Map<String, Object> tree, final boolean parallel, final List<String> superConfig) {
        if (tree.containsKey(KEY_TYPE)) {
            String key = KEY_TYPE;
            switch ((String) tree.get(key)) {
                case STEP_TYPE_COMMAND: {
                    final String str = createCommandStep(tab, (String) tree.get(KEY_VALUE));
                    System.out.print("\n" + str + "\n");
                }
                break;
                case STEP_TYPE_SEQ: {
                    createAndPrintSuperConfig(tab, tree, superConfig);
                    if (tree.containsKey(KEY_STEPS)) {
                        print(tab + TAB, (ArrayList<Object>) tree.get(KEY_STEPS), false, superConfig);
                    } else {
                        print(tab + TAB, (ArrayList<Object>) tree.get(KEY_JOBS), false, superConfig);
                    }
                    if (((Map<String, Object>) tree).containsKey(KEY_CONFIG))
                        superConfig.remove(superConfig.size() - 1);
                }
                break;
                case STEP_TYPE_PARALLEL: {
                    createAndPrintSuperConfig(tab, tree, superConfig);
                    String key_steps = null;
                    if (tree.containsKey(KEY_STEPS)) {
                        key_steps = KEY_STEPS;
                    } else {
                        key_steps = KEY_JOBS;
                    }
                    final int stepsCount = ((ArrayList<Object>) tree.get(key_steps)).size();
                    print(tab, (ArrayList<Object>) tree.get(key_steps), true, superConfig);
                    if (((Map<String, Object>) tree).containsKey(KEY_CONFIG))
                        superConfig.remove(superConfig.size() - 1);
                    final String str = createParallelSteps(tab, stepsCount);
                    System.out.print("\n" + str + "\n");
                }
                break;
                case STEP_TYPE_PRECONDITION: {
                    final String str = createPrecondStep(tab, (Map<String, Object>) tree.get(KEY_CONFIG), superConfig);
                    System.out.print("\n" + str + "\n");
                }
                break;
                case STEP_TYPE_FOR: {
                    //createAndPrintSuperConfig(tab, tree, superConfig);
                    String str = null;
                    if (tree.containsKey(KEY_VALUE)) {
                        final Object val = tree.get(KEY_VALUE);
                        if (tree.containsKey(KEY_IN)) {
                            final Object in = tree.get(KEY_IN);
                            if (in instanceof List) //by seq
                                str = createForStep(tab, (String) val, (List) in);
                            else //by range
                                str = createForStep(tab, (String) val, (String) in);
                        } else //by count
                            str = createForStep(tab, val.toString());
                    } else //infinite
                        str = createForStep(tab);

                    System.out.print("\n" + str + "\n");
                    createAndPrintSuperConfig(tab, tree, superConfig);
                    if (tree.containsKey(KEY_STEPS)) {
                        print(tab + TAB, (ArrayList<Object>) tree.get(KEY_STEPS), false, superConfig);
                    } else {
                        print(tab + TAB, (ArrayList<Object>) tree.get(KEY_JOBS), false, superConfig);
                    }
                    if (((Map<String, Object>) tree).containsKey(KEY_CONFIG))
                        superConfig.remove(superConfig.size() - 1);
                    System.out.println(tab + "};");
                }
                break;
                case STEP_TYPE_LOAD: {
                    final String str = createStepLoad(tab, (Map<String, Object>) tree.get(KEY_CONFIG), superConfig);
                    System.out.print("\n" + str + "\n");
                }
                break;
                default:
                    System.out.println(tab + "<" + tree.get(key) + ">");
            }
        }
    }

    private static void createAndPrintSuperConfig(final String tab, final Object tree, final List superConfig) {
        if (!((Map<String, Object>) tree).containsKey(KEY_CONFIG)) return;
        final Object config = ((Map<String, Object>) tree).get(KEY_CONFIG);
        final String str = createSuperConfig(tab, (Map<String, Object>) config);
        superConfig.add("superConfig_" + superConfigCounter);
        System.out.print("\n" + str + "\n");

    }

    private static String createSuperConfig(final String tab, final Map<String, Object> config) {
        return tab + "var superConfig_" + superConfigCounter.incrementAndGet() + " = " +
                convertConfig(tab, config) + ";";
    }

    private static void print(final String tab, final ArrayList<Object> steps, final boolean parallel, final List<String> superConfig) {
        for (Object step : steps) {
            if (step instanceof Map) {
                if (parallel)
                    System.out.println("\n" + tab + "function func" + (parallelCounter.incrementAndGet()) + "() {");
                print(tab + TAB, (Map<String, Object>) step, parallel, superConfig);
                if (parallel) System.out.println(tab + "};");
            }
        }
    }

    private static void replaceVariables(final Map<String, Object> tree) {
        for (String key : tree.keySet()) {
            if (key.equals(KEY_TYPE) && tree.get(key).equals(STEP_TYPE_COMMAND))
                break;
            else if (tree.get(key) instanceof String) {
                for (String var : oldScenario.getAllVarList()) {
                    tree.replace(key, ((String) tree.get(key)).replaceAll(String.format(VAR_PATTERN, var), var));
                }
            } else replaceVariables(tree.get(key));
        }
    }

    private static void replaceVariables(final List<Object> list) {
        if (list.isEmpty()) return;
        if (list.get(0) instanceof Map) {
            for (Object item : list) {
                replaceVariables((Map<String, Object>) item);
            }
        } else {
            for (String var : oldScenario.getAllVarList()) {
                Collections.replaceAll(list, String.format(VAR_FORMAT, var), var);
            }
        }
    }

    private static void replaceVariables(final Object o) {
        if (o instanceof Map)
            replaceVariables((Map) o);
        else if (o instanceof List)
            replaceVariables((List) o);
    }

    private static String createCommandStep(final String tab, final String cmdLine) {
        String newCmdLine = cmdLine;
        for (String var : oldScenario.getLoopVarList()) {
            newCmdLine = newCmdLine.replaceAll(String.format(VAR_PATTERN, var), "\" + " + var + " + \"");
        }
        return String.format(COMMAND_FORMAT, tab, cmdCounter.incrementAndGet(), tab, "\"" + newCmdLine + "\"", tab);
    }

    private static String createParallelSteps(final String tab, final int stepCount) {
        String str = tab + THREAD_TYPE_FORMAT;
        for (int s = 1; s <= stepCount; ++s) {
            str += String.format(NEW_THREAD_FORMAT, tab, s, s, tab, s);
        }
        for (int s = 1; s <= stepCount; ++s) {
            str += String.format(JOIN_FORMAT, tab, s);
        }
        return str;
    }

    private static String createSeqVariable(final String varName, final List seq) {
        return "var " + varName + SEQ_POSTFIX + " = " + seq.toString() + ";";
    }

    private static String createForStep(final String tab, final String varName, final List seq) {
        String str = tab + createSeqVariable(varName, seq) + "\n";
        str += tab + String.format(FORIN_FORMAT, varName, varName + SEQ_POSTFIX);
        return str;
    }

    private static String createForStep(final String tab, final String varName, final String range) {
        final String startVal = range.split("-")[0];
        final String endVal = range.split("-")[1].split(",")[0];
        final String step = range.split("-")[1].split(",")[1];
        return tab + createForLine(varName, startVal, endVal, step);
    }

    private static String createForStep(final String tab, final String val) {
        final String loopVar = "i" + (forCounter.incrementAndGet());
        return tab + createForLine(loopVar, "0", val, "1");
    }

    private static String createForLine(final String varName, final String startValue, final String endValue, final String step) {
        return String.format(FOR_FORMAT, varName, startValue, varName, endValue, varName, step);
    }

    private static String createForStep(String tab) {
        return tab + WHILE_FORMAT;
    }

    private static String createStepLoad(final String tab, final Map<String, Object> config, final List<String> superConfig) {
        final String varName = "step_" + (stepCounter.incrementAndGet());
        String str = tab;
        final String type = (config != null) ? ConfigConverter.pullLoadType(config) : "";
        //TODO: verifyLoad, precondition, mixed ... and others
        switch (type) {
            case KEY_CREATE: {
                str += "var " + varName + " CreateLoad();\n";
            }
            break;
            case KEY_READ: {
                str += "var " + varName + " ReadLoad();\n";
            }
            break;
            case KEY_UPDATE: {
                str += "var " + varName + " UpdateLoad();\n";
            }
            break;
            case KEY_DELETE: {
                str += "var " + varName + " DeleteLoad();\n";
            }
            break;
            default: {
                str += "var " + varName + " Load();\n";
            }
        }
        for (String configName : superConfig) {
            str += tab + varName + ".config(" + configName + ");\n";
        }
        if (config != null)
            str += tab + varName + ".config(" + convertConfig(tab + TAB, config) + ");\n";
        str += tab + varName + ".start();";
        return str;
    }

//    private static String createStepLoad(final String tab, final String loadType, final Map<String, Object> config, final List<String> superConfig) {
//        switch ()
//        final String varName = "step_" + (stepCounter.incrementAndGet());
//        String str = tab;
//        for (String configName : superConfig) {
//            str += tab + varName + ".config(" + configName + ");\n";
//        }
//        if (config != null)
//            str += tab + varName + ".config(" + convertConfig(tab + TAB, config) + ");\n";
//        str += tab + varName + ".start();";
//        return str;
//    }

    private static String createPrecondStep(final String tab, final Map<String, Object> config, final List<String> superConfig) {
        final String varName = "step_" + (stepCounter.incrementAndGet());
        String str = tab + "var " + varName + " = PreconditionLoad();\n";
        for (String configName : superConfig) {
            str += tab + varName + ".config(" + configName + ");\n";
        }
        if (config != null)
            str += tab + varName + ".config(" + convertConfig(tab + TAB, config) + ");\n";
        str += tab + varName + ".start();";
        return str;
    }

    private static String convertConfig(final String tab, final Map map) {
        String str = ConfigConverter.convertConfigAndToJson(map);
        str = str.replaceAll("\\n", "\n" + tab);
        for (String var : oldScenario.getAllVarList()) {
            str = str.replaceAll(String.format(QUOTES_PATTERN, var), var);
        }
        return str;
    }
}
