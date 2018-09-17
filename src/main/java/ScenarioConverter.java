import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class ScenarioConverter {

	private static final AtomicInteger stepCounter = new AtomicInteger(0);
	private static final AtomicInteger cmdCounter = new AtomicInteger(0);
	private static final AtomicInteger forCounter = new AtomicInteger(0);
	private static final AtomicInteger parallelCounter = new AtomicInteger(0);
	private static final AtomicInteger parentConfigCounter = new AtomicInteger(0);
	private static Scenario oldScenario;

	public static void print(final Scenario scenario) {
		oldScenario = scenario;
		final Map<String, Object> tree = oldScenario.getStepTree();
		replaceVariables(tree);
		replaceJobsOnSteps(tree);
		print("", tree, new ArrayList<>());
	}

	private static void print(final String tab, final Map<String, Object> tree, final List<String> parentConfig) {
		if(tree.containsKey(Constants.KEY_TYPE)) {
			String key = Constants.KEY_TYPE;
			switch((String) tree.get(key)) {
				case Constants.STEP_TYPE_COMMAND: {
					final String str = createCommandStep(tab, (String) tree.get(Constants.KEY_VALUE));
					System.out.print(str + "\n\n");
				}
				break;
				case Constants.STEP_TYPE_CHAIN: {
					final String str = createPipelineLoad(tab, (List) tree.get(Constants.KEY_CONFIG), parentConfig);
					System.out.print(str + "\n\n");
				}
				break;
				case Constants.STEP_TYPE_MIXED: {
					final String str = createWeightedLoad(tab, tree, parentConfig);
					System.out.print(str + "\n\n");
				}
				break;
				case Constants.STEP_TYPE_SEQ: {
					createAndPrintParentConfig(tab, tree, parentConfig);
					print(tab, (ArrayList<Object>) tree.get(Constants.KEY_STEPS), false, parentConfig);
					if(tree.containsKey(Constants.KEY_CONFIG)) {
						parentConfig.remove(parentConfig.size() - 1);
					}
				}
				break;
				case Constants.STEP_TYPE_PARALLEL: {
					createAndPrintParentConfig(tab, tree, parentConfig);
					String key_steps = null;
					key_steps = Constants.KEY_STEPS;
					final int stepsCount = ((ArrayList<Object>) tree.get(key_steps)).size();
					print(tab, (ArrayList<Object>) tree.get(key_steps), true, parentConfig);
					if(tree.containsKey(Constants.KEY_CONFIG)) {
						parentConfig.remove(parentConfig.size() - 1);
					}
					final String str = createParallelSteps(tab, stepsCount);
					System.out.print(str + "\n");
				}
				break;
				case Constants.STEP_TYPE_FOR: {
					String str = null;
					if(tree.containsKey(Constants.KEY_VALUE)) {
						final Object val = tree.get(Constants.KEY_VALUE);
						if(tree.containsKey(Constants.KEY_IN)) {
							final Object in = tree.get(Constants.KEY_IN);
							if(in instanceof List) //by seq
							{
								str = createForStep(tab, (String) val, (List) in);
							} else //by range
							{
								str = createForStep(tab, (String) val, (String) in);
							}
						} else //by count
						{
							str = createForStep(tab, val.toString());
						}
					} else //infinite
					{
						str = createForStep(tab);
					}
					System.out.print(str + "\n");
					createAndPrintParentConfig(tab, tree, parentConfig);
					print(tab + Constants.TAB, (ArrayList<Object>) tree.get(Constants.KEY_STEPS), false, parentConfig);
					if(((Map<String, Object>) tree).containsKey(Constants.KEY_CONFIG)) {
						parentConfig.remove(parentConfig.size() - 1);
					}
					System.out.println(tab + "};");
				}
				break;
				case Constants.STEP_TYPE_LOAD: {
					final String str =
						createStepLoad(tab, (Map<String, Object>) tree.get(Constants.KEY_CONFIG), parentConfig, false);
					System.out.print(str + "\n\n");
				}
				break;
				case Constants.STEP_TYPE_PRECONDITION: {
					final String str =
						createStepLoad(tab, (Map<String, Object>) tree.get(Constants.KEY_CONFIG), parentConfig, true);
					System.out.print(str + "\n\n");
				}
				break;
				default:
					System.out.println(tab + "<" + tree.get(key) + ">");
			}
		}
	}

	private static void print(
		final String tab, final ArrayList<Object> steps, final boolean parallel, final List<String> parentConfig
	) {
		for(Object step : steps) {
			if(step instanceof Map) {
				if(parallel) {
					createParallelFunction(tab, step, parentConfig);
				} else {
					print(tab, (Map<String, Object>) step, parentConfig);
				}
			}
		}
	}

	private static String createWeightedLoad(
		final String tab, final Map<String, Object> tree, final List<String> parentConfig
	) {
		final List<Map<String, Object>> configsRaw = (List) tree.get(Constants.KEY_CONFIG);
		final List<Map<String, Object>> configs = new ArrayList<>();
		final String tabb = tab + Constants.TAB;
		for(Map config : configsRaw) {
			configs.add(ConfigConverter.convertConfig(config));
		}
		final List weights = (List) tree.get(Constants.KEY_WEIGHTS);
		if(weights == null) {
			System.err.println("< ERROR: This scenario can't be converted : Mixed Load must have weights >");
		}
		String str = tab + "WeightedLoad\n";
		str += createConfigs(tabb, configs, parentConfig, weights);
		str += tabb + ".run();";
		return str;
	}

	private static String createPipelineLoad(
		final String tab, final List<Map<String, Object>> configs, final List<String> parentConfig
	) {
		String str = tab + "PipelineLoad\n";
		final String tabb = tab + Constants.TAB;
		str += createConfigs(tabb, configs, parentConfig, null);
		str += tabb + ".run();";
		return str;
	}

	private static String createConfigs(
		final String tab, final List<Map<String, Object>> configs, final List<String> parentConfig, final List weights
	) {
		String str = new String();
		//parent config
		for(String configName : parentConfig) {
			str += tab + ".config(" + configName + ")\n";
		}
		//common config
		for(int i = 0; i < configs.size(); ++ i) {
			if(i == 0) {
				final String loadStepSection = pullLoadSection(tab, configs.get(i));
				if(loadStepSection != null) {
					str += tab + ".config(" + loadStepSection + ")\n";
				}
			}
			//substep config
			if(weights != null) {
				str += tab + ".append(" + convertConfig(tab, configs.get(i), weights.get(i)) + ")\n";
			} else {
				str += tab + ".append(" + convertConfig(tab, configs.get(i)) + ")\n";
			}
		}
		return str;
	}

	private static void createParallelFunction(final String tab, final Object step, final List<String> parentConfig) {
		System.out.print(tab + "function func" + (parallelCounter.incrementAndGet()) + "() {\n");
		print(tab + Constants.TAB, (Map<String, Object>) step, parentConfig);
		System.out.println(tab + "};\n");
	}

	private static void createAndPrintParentConfig(final String tab, final Object tree, final List parentConfig) {
		if(! ((Map<String, Object>) tree).containsKey(Constants.KEY_CONFIG)) {
			return;
		}
		final Object config = ((Map<String, Object>) tree).get(Constants.KEY_CONFIG);
		final String str = createParentConfig(tab, (Map<String, Object>) config);
		parentConfig.add("parentConfig_" + parentConfigCounter);
		System.out.print(str + "\n\n");
	}

	private static String createParentConfig(final String tab, final Map<String, Object> config) {
		return tab + "var parentConfig_" + parentConfigCounter.incrementAndGet() + " = " + convertConfig(tab, config) +
			";";
	}

	private static void replaceVariables(final Map<String, Object> tree) {
		for(String key : tree.keySet()) {
			if(key.equals(Constants.KEY_TYPE) && tree.get(key).equals(Constants.STEP_TYPE_COMMAND)) {
				break;
			} else if(tree.get(key) instanceof String) {
				for(String var : oldScenario.getAllVarList()) {
					if(key.equals(Constants.KEY_FILE) || key.equals(Constants.KEY_PATH) ||
						key.equals(Constants.KEY_ID)) {
						tree.replace(key, ((String) tree.get(key)).replaceAll(String.format(Constants.VAR_PATTERN, var),
							"\" + " + var + " + \""
						));
						tree.replace(key, ((String) tree.get(key)).replaceAll(var, "\"" + var + "\""));
					} else {
						tree.replace(
							key, ((String) tree.get(key)).replaceAll(String.format(Constants.VAR_PATTERN, var), var));
					}
				}
			} else {
				replaceVariables(tree.get(key));
			}
		}
	}

	private static void replaceVariables(final List<Object> list) {
		if(list.isEmpty()) {
			return;
		}
		if(list.get(0) instanceof Map) {
			for(Object item : list) {
				replaceVariables((Map<String, Object>) item);
			}
		} else {
			for(String var : oldScenario.getAllVarList()) {
				Collections.replaceAll(list, String.format(Constants.VAR_FORMAT, var), var);
			}
		}
	}

	private static void replaceVariables(final Object o) {
		if(o instanceof Map) {
			replaceVariables((Map) o);
		} else if(o instanceof List) {
			replaceVariables((List) o);
		}
	}

	private static String createCommandStep(final String tab, final String cmdLine) {
		String newCmdLine = cmdLine;
		newCmdLine = newCmdLine.replaceAll("\\\"", "'");
		for(String var : oldScenario.getLoopVarList()) {
			newCmdLine = newCmdLine.replaceAll(String.format(Constants.VAR_PATTERN, var), "\" + " + var + " + \"");
		}
		final String varName = "cmd_" + cmdCounter.incrementAndGet();
		return String.format(Constants.COMMAND_FORMAT, tab, cmdCounter.get(), tab, "\"" + newCmdLine + "\"", tab, tab) +
			"\n" + tab + varName + ".waitFor();";
	}

	private static String createParallelSteps(final String tab, final int stepCount) {
		String str = tab + Constants.THREAD_TYPE_FORMAT;
		for(int s = 1; s <= stepCount; ++ s) {
			str += String.format(Constants.NEW_THREAD_FORMAT, tab, s, s, tab, s);
		}
		for(int s = 1; s <= stepCount; ++ s) {
			str += String.format(Constants.JOIN_FORMAT, tab, s);
		}
		return str;
	}

	private static String createSeqVariable(final String varName, final List seq) {
		String newSeq = seq.stream().map(s -> {
			return (s instanceof String && ! oldScenario.getAllVarList().contains(s)) ? String.format("\"%s\"", s) : s;
		}).collect(Collectors.toList()).toString();
		return "var " + varName + Constants.SEQ_POSTFIX + " = " + newSeq + ";";
	}

	private static String createForStep(final String tab, final String varName, final List seq) {
		String str = tab + createSeqVariable(varName, seq) + "\n";
		str += tab + String.format(Constants.FORIN_FORMAT, varName, varName + Constants.SEQ_POSTFIX);
		return str;
	}

	private static String createForStep(final String tab, final String varName, final String range) {
		final String startVal = range.split("-")[0];
		final String endVal = range.split("-")[1].split(",")[0];
		if(range.split("-")[1].split(",").length != 1) {
			final String step = range.split("-")[1].split(",")[1];
		}
		final String step = "1";
		return tab + createForLine(varName, startVal, endVal, step);
	}

	private static String createForStep(final String tab, final String val) {
		final String loopVar = "i" + (forCounter.incrementAndGet());
		return tab + createForLine(loopVar, "0", val, "1");
	}

	private static String createForLine(
		final String varName, final String startValue, final String endValue, final String step
	) {
		return String.format(Constants.FOR_FORMAT, varName, startValue, varName, endValue, varName, step);
	}

	private static String createForStep(String tab) {
		return tab + Constants.WHILE_FORMAT;
	}

	private static String createStepLoad(
		final String tab, final Map<String, Object> config, final List<String> parentConfig,
		final boolean isPrecondition
	) {
		String str = tab;
		if(isPrecondition) {
			str += "PreconditionLoad\n";
		} else {
			final String type = (config != null) ? ConfigConverter.pullLoadType(config) : "";
			switch(type) {
				case Constants.KEY_CREATE: {
					str += "CreateLoad\n";
				}
				break;
				case Constants.KEY_READ: {
					str += "ReadLoad\n";
				}
				break;
				case Constants.KEY_UPDATE: {
					str += "UpdateLoad\n";
				}
				break;
				case Constants.KEY_DELETE: {
					str += "DeleteLoad\n";
				}
				break;
				default: {
					str += "Load\n";
				}
			}
		}
		for(String configName : parentConfig) {
			str += tab + Constants.TAB + ".config(" + configName + ")\n";
		}
		if(config != null) {
			str += tab + Constants.TAB + ".config(" + convertConfig(tab + Constants.TAB, config) + ")\n";
		}
		str += tab + Constants.TAB + ".run();";
		return str;
	}

	private static String convertConfig(final String tab, final Map map) {
		String str = ConfigConverter.convertConfigAndToJson(map);
		str = str.replaceAll("\\n", "\n" + tab);
		str = removeQuotes(str);
		return str;
	}

	private static String pullLoadSection(final String tab, final Map map) {
		String str = ConfigConverter.pullLoadStepSectionStr(map);
		if(str == null) {
			return null;
		}
		str = str.replaceAll("\\n", "\n" + tab);
		str = removeQuotes(str);
		return str;
	}

	private static String convertConfig(final String tab, final Map<String, Object> map, final Object weight) {
		final Map config = ConfigConverter.addWeight(ConfigConverter.convertConfig(map), weight);
		return convertConfig(tab, config);
	}

	private static String removeQuotes(final String str) {
		String tmp = str;
		for(String var : oldScenario.getAllVarList()) {
			tmp = tmp.replaceAll(String.format(Constants.QUOTES_PATTERN, var), var);
		}
		return tmp;
	}

	private static void replaceJobsOnSteps(final Map<String, Object> tree) {
		for(String key : tree.keySet()) {
			if(key.equals(Constants.KEY_JOBS)) {
				tree.put(Constants.KEY_STEPS, tree.remove(Constants.KEY_JOBS));
				for(Object item : (List) tree.get(Constants.KEY_STEPS)) {
					if(item instanceof Map) {
						replaceJobsOnSteps((Map<String, Object>) item);
					}
				}
			}
		}
	}
}
