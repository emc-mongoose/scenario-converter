public interface IConverter {
    final String STEP_TYPE_FOR = "for";
    final String STEP_TYPE_COMMAND = "command";
    final String STEP_TYPE_SEQ = "sequential";
    final String STEP_TYPE_LOAD = "load";
    final String STEP_TYPE_PARALLEL = "parallel";
    final String STEP_TYPE_PRECONDITION = "precondition";

    final String KEY_STEPS = "steps";
    final String KEY_TYPE = "type";
    final String KEY_CONFIG = "config";
    final String KEY_VALUE = "value";
    final String KEY_IN = "in";

    final String VAR_PATTERN = "\\$\\{%s\\}";
    final String WITHOUT_SPACES_PATTERN = "([\\w\\-_.!@#%\\^&*=+()\\[\\]~:;'\\\\|/<>,?]+)";
    final String QUOTES_PATTERN = "\"%s\"";
    final String TAB = "    ";
    final String INDEX_POSTFIX = "_i";
    final String FOR_FORMAT = "for( var %s = 0; %s < %s.length; ++%s ){";
    final String COMMAND_FORMAT = "%s var cmd_%d = new java.lang.ProcessBuilder()\n%s.command(\"sh\", \"-c\", %s)\n%s.start();";
}
