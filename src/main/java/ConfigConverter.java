import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.akurilov.commons.collection.TreeUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigConverter {

	private static Map<String, String> params;

	static {
		try {
			params = Files.lines(
				Paths.get(Paths.get("")
					.toAbsolutePath()
					.toString() + "/build/resources/main/configChanging")).filter(s -> ! s.isEmpty())
				.collect(Collectors.toMap(k -> k.split("\\s+")[0], v -> v.split("\\s+")[1]));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static String convertConfig(final String oldConfig) {
		String str = null;
		try {
			final Map<String, Object> map =
				new ObjectMapper()
					.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
					.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true)
					.readValue(oldConfig, new TypeReference<Map<String, Object>>() {
					});
			str = convertConfigAndToJson(map);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static Map<String, Object> convertConfig(final Map<String, Object> oldConfig) {
		final Map<String, Object> newConfig = TreeUtil.copyTree(oldConfig);
		convert(oldConfig, newConfig);
		return newConfig;
	}

	private static String replaceArrays(final String str) {
		final Pattern p = Pattern.compile(":\\s*\\[[^\\]]*\\]");
		final Matcher m = p.matcher(str);
		String result = str;
		while(m.find()) {
			final String entry = m.group(0).split("\\[|\\]")[1];
			result = str.replaceAll("\\[" + entry + "\\]", "new java.util.ArrayList([" + entry + "])");
		}
		return result;
	}

	public static String pullLoadType(final Map<String, Object> config) {
		final List keys = new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_TYPE));
		final String type = (String) getValuesFromSection(keys, config);
		if(type != null) {
			deleteSection(keys, config);
			return type;
		} else {
			return new String();
		}
	}

	private static void convert(final Map<String, Object> tree, final Map<String, Object> newTree) {
		for(String k : params.keySet()) {
			final List oldParams = new ArrayList<>(Arrays.asList(k.split(Constants.PARAM_DELIMITER)));
			final List newParams = new ArrayList<>(Arrays.asList(params.get(k).split(Constants.PARAM_DELIMITER)));
			final Object values = getValuesFromSection(oldParams, tree);
			if(values != null) {
				setValuesIntoSection(newParams, values, newTree);
				deleteSection(oldParams, newTree);
			}
		}
	}

	private static void deleteSection(final List<String> keys, final Map<String, Object> tree) {
		if(keys.size() == 1) {
			tree.remove(keys.get(0));
		} else {
			final String key = keys.remove(0);
			deleteSection(keys, (Map<String, Object>) tree.get(key));
		}
	}

	private static void setValuesIntoSection(
		final List<String> keys, final Object values, final Map<String, Object> tree
	) {
		final List<String> keyss = new ArrayList<>(keys);
		final String key = keyss.remove(0);
		if(! tree.containsKey(key)) {
			tree.put(key, new HashMap<String, Object>());
		}
		if(keyss.isEmpty()) {
			if(values instanceof Map) {
				((Map<String, Object>) tree.get(key)).putAll((Map) values);
			} else {
				tree.put(key, values);
			}
		} else {
			setValuesIntoSection(keyss, values, (Map<String, Object>) tree.get(key));
		}
	}

	private static Object getValuesFromSection(final List<String> keys, final Map<String, Object> tree) {
		Object value = null;
		final List<String> keyss = new ArrayList<>(keys);
		final String key = keyss.remove(0);
		if(tree.keySet().contains(key)) {
			final String p;
			if(keyss.isEmpty()) {
				value = tree.get(key);
			} else {
				if(tree.get(key) instanceof Map) {
					value = getValuesFromSection(keyss, (Map<String, Object>) tree.get(key));
				}
			}
		}
		return value;
	}

	public static String convertConfigAndToJson(final Map<String, Object> oldConfig) {
		return mapToStr(convertConfig(oldConfig));
	}

	public static String mapToStr(final Map<String, Object> map) {
		String str = null;
		try {
			str =
				new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map).replaceAll("\\\\\"", "\"");
			str = replaceArrays(str);
		} catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static Map addWeight(final Map config, final Object weight) {
		final Map newConfig = config;
		final Map w = new HashMap();
		w.put(Constants.KEY_WEIGHT, weight);
		setValuesIntoSection(new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_GENERATOR)), w, newConfig);
		return newConfig;
	}

	public static Map pullLoadStepSection(final Map config) {
		final List keys = new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_STEP));
		final Map section = (Map) getValuesFromSection(keys, config);
		deleteSection(keys, config);
		return section;
	}

	public static String pullLoadStepSectionStr(final Map config) {
		return mapToStr(pullLoadStepSection(config));
	}
}
