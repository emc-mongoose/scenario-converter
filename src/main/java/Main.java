import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.emc.mongoose.api.common.Constants.DIR_EXAMPLE_SCENARIO;
import static com.emc.mongoose.api.common.env.PathUtil.getBaseDir;

public class Main {

    private static Path scenarioPath;

    public static void main(String[] args) throws IOException {

        if (args.length != 0) {
            scenarioPath = Paths.get(getBaseDir(), args[0]);
        }
        Path scenarioPath = Paths.get(DIR_EXAMPLE_SCENARIO, "json", "test.json");
        Converter converter = new Converter(scenarioPath);
        converter.print();
    }

}
