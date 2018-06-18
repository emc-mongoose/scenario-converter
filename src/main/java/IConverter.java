public interface IConverter {
    String STEP_TYPE_FOR = "for";
    String STEP_TYPE_COMMAND = "command";
    String STEP_TYPE_SEQ = "sequential";
    String STEP_TYPE_LOAD = "load";
    String STEP_TYPE_PARALLEL = "parallel";
    String STEP_TYPE_PRECONDITION = "precondition";

    String KEY_STEPS = "steps";
    String KEY_STEP = "step";
    String KEY_TYPE = "type";
    String KEY_CONFIG = "config";
    String KEY_LOAD = "load";
    String KEY_VALUE = "value";
    String KEY_IN = "in";
    String KEY_TEST = "test";
    String KEY_RUN = "run";

    String VAR_PATTERN = "\\$\\{%s\\}";
    String WITHOUT_SPACES_PATTERN = "([\\w\\-_.!@#%\\^&*=+()\\[\\]~:;'\\\\|/<>,?]+)";
    String QUOTES_PATTERN = "\"%s\"";
    String TAB = "    ";
    String INDEX_POSTFIX = "_i";
    String FOR_FORMAT = "for( var %s = 0; %s < %s.length; ++%s ){";
    String COMMAND_FORMAT = "%s var cmd_%d = new java.lang.ProcessBuilder()\n%s.command(\"sh\", \"-c\", %s)\n%s.start();";
}
