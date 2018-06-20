import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        final Path scenarioPath = (args.length != 0)
                ? Paths.get(args[0])
                //: Paths.get("example", "scenario", "json", "test.json");
                //: Paths.get("example", "scenario", "json", "loop", "by-count-arg.json");
                : Paths.get("example", "scenario", "json", "dynamic", "read-files-from-variable-path.json");
        ScenarioConverter.print(new Scenario(Scenario.parseJson(scenarioPath.toFile())));
    }
}
