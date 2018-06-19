public interface Converter {
    String STEP_TYPE_FOR = "for";
    String STEP_TYPE_COMMAND = "command";
    String STEP_TYPE_SEQ = "sequential";
    String STEP_TYPE_LOAD = "load";
    String STEP_TYPE_PARALLEL = "parallel";
    String STEP_TYPE_PRECONDITION = "precondition";

    String KEY_CONFIG = "config";
    String KEY_IN = "in";
    String KEY_LIMIT = "limit";
    String KEY_LOAD = "load";
    String KEY_RUN = "run";
    String KEY_SCENARIO = "scenario";
    String KEY_STEP = "step";
    String KEY_STEPS = "steps";
    String KEY_TEST = "test";
    String KEY_TYPE = "type";
    String KEY_VALUE = "value";

    String VAR_PATTERN = "\\$\\{%s\\}";
    String WITHOUT_SPACES_PATTERN = "([\\w\\-_.!@#%\\^&*=+()\\[\\]~:;'\\\\|/<>,?]+)";
    String QUOTES_PATTERN = "\"%s\"";
    String TAB = "    ";
    String SEQ_POSTFIX = "_seq";
    String VAR_FORMAT = "${%s}";
    String FOR_FORMAT = "for( var %s = %s; %s < %s; %s += %s ){";
    String FORIN_FORMAT = "for( %s in %s ){";
    String WHILE_FORMAT = "while( true ){";
    String COMMAND_FORMAT = "%s var cmd_%d = new java.lang.ProcessBuilder()\n%s.command(\"sh\", \"-c\", %s)\n%s.start();";
    String THREAD_TYPE_FORMAT = "var Thread = Java.type('java.lang.Thread');\n";
    String NEW_THREAD_FORMAT = "%sthread%d  = new Thread(func%d);\n%sthread%d.start();\n";
    String JOIN_FORMAT = "%sthread%d.join();\n";
}
