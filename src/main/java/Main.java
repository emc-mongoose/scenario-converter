import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        final Path scenarioPath = (args.length != 0)
                ? Paths.get(args[0])
                : Paths.get("example", "scenario", "json", "test.json");
        ScenarioConverter.print(new Scenario(Scenario.parseJson(scenarioPath.toFile())));
    }
}
