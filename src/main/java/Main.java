import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            ScenarioConverter.print(new Scenario(Scenario.parseJson(Paths.get(arg).toFile())));
        }
        ScenarioConverter.print(new Scenario(Scenario.parseJson(Paths.get("example", "scenario", "json", "misc", "rampup.json").toFile())));
    }
}
