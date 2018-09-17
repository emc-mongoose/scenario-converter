import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static final String MULTIPLE_PATH_KEY = "--m";

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            if (args[0].equals(MULTIPLE_PATH_KEY)) {
                for (int i = 1; i < args.length; ++i) {
                    final Path path = Paths.get(args[i]);
                    final String newPath = path.toString().replaceAll("\\.json", ".js");
                    final PrintStream out = new PrintStream(
                            new FileOutputStream(newPath));
                    System.setOut(out);
                    ScenarioConverter.print(new Scenario(Scenario.parseJson(path.toFile())));
                }
            } else {
                ScenarioConverter.print(new Scenario(Scenario.parseJson(Paths.get(args[0]).toFile())));
            }
        } else {
            Logger.getGlobal().warning("No scenario-file");

        }
    }
}
