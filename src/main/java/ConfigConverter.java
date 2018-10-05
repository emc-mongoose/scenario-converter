import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.akurilov.commons.collection.TreeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigConverter {

	private final static Map<String, String> params = new HashMap<>();

	static {
		try(
			final InputStream in = ConfigConverter.class.getResource("configChanging").openStream();
			final BufferedReader br = new BufferedReader(new InputStreamReader(in))
		) {
			String line;
			while(null != (line = br.readLine())) {
				if(!line.isEmpty()) {
					final String[] lineParts = line.split("\\s+");
					params.put(lineParts[0], lineParts[1]);
				}
			}
		} catch(final IOException e) {
			e.printStackTrace();
		}
	}

	public static String convertConfig(final String oldConfig) {
		String str = null;
		try {
			final Map<String, Object> map =
				new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true).configure(
					JsonParser.Feature.ALLOW_YAML_COMMENTS, true).readValue(
					oldConfig, new TypeReference<Map<String, Object>>() {
					});
			str = convertConfigAndToJson(map);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	static Map<String, Object> convertConfig(final Map<String, Object> oldConfig) {
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

	static String pullLoadType(final Map<String, Object> config) {
		final List<String> keys = new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_TYPE));
		final String type = (String) getValuesFromSection(keys, config);
		if(type != null) {
			deleteSection(keys, config);
			return type;
		} else {
			return "";
		}
	}

	private static void convert(final Map<String, Object> tree, final Map<String, Object> newTree) {
		for(String k : params.keySet()) {
			final List<String> oldParams = new ArrayList<>(Arrays.asList(k.split(Constants.PARAM_DELIMITER)));
			final List<String> newParams = new ArrayList<>(Arrays.asList(params.get(k).split(Constants.PARAM_DELIMITER)));
			for (Map<String, Object> t : Arrays.asList(tree,newTree)) {
				final Object values = getValuesFromSection(oldParams, t);
				if(values != null) {
					setValuesIntoSection(newParams, values, newTree);
					deleteSection(oldParams, newTree);
				}
			}
		}
	}

	private static void deleteSection(final List<String> keys, final Map<String, Object> tree) {
		if(keys.size() == 1) {
			tree.remove(keys.get(0));
		} else {
			final List<String> keyss = new ArrayList<>(keys);
			final String key = keyss.remove(0);
			deleteSection(keyss, (Map<String, Object>) tree.get(key));
			if(((Map<String, Object>) tree.get(key)).isEmpty()) {
				tree.remove(key);
			}
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

	static String convertConfigAndToJson(final Map<String, Object> oldConfig) {
		return mapToStr(convertConfig(oldConfig));
	}

	private static String mapToStr(final Map<String, Object> map) {
		if(map == null) {
			return null;
		}
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

	static Map<String, Object> addWeight(final Map<String, Object> config, final Object weight) {
		final Map<String, Object> newConfig = config;
		final Map<String, Object> w = new HashMap<>();
		w.put(Constants.KEY_WEIGHT, weight);
		setValuesIntoSection(new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_GENERATOR)), w, newConfig);
		return newConfig;
	}

	private static Map<String, Object> pullLoadStepSection(final Map config) {
		final List<String> keys = new ArrayList<>(Arrays.asList(Constants.KEY_LOAD, Constants.KEY_STEP));
		final Map values = (Map) getValuesFromSection(keys, config);
		if(values == null) {
			return null;
		}
		deleteSection(keys, config);
		final Map<String, Object> section = new HashMap<String, Object>();
		setValuesIntoSection(keys, values, section);
		return section;
	}

	static String pullLoadStepSectionStr(final Map config) {
		return mapToStr(pullLoadStepSection(config));
	}
}
