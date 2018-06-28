import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static String MULTIPLE_PATH_KEY = "--m";
    public static String RESULT_DIRECTORY_PATH = "results";

    public static void main(String[] args) throws IOException {
        try {
            if (args[0].equals(MULTIPLE_PATH_KEY)) {
                for (int i = 1; i < args.length; ++i) {
                    final Path pathToFile = Paths.get(args[i]);
                    final String fileName = pathToFile.getFileName().toString().split("\\.")[0];
                    if (!(new File(RESULT_DIRECTORY_PATH)).exists())
                        (new File(RESULT_DIRECTORY_PATH)).mkdirs();
                    final PrintStream out = new PrintStream(
                            new FileOutputStream(Paths.get(RESULT_DIRECTORY_PATH, fileName + ".js").toString()));
                    System.setOut(out);
                    ScenarioConverter.print(new Scenario(Scenario.parseJson(pathToFile.toFile())));
                }
            } else {
                ScenarioConverter.print(new Scenario(Scenario.parseJson(Paths.get(args[0]).toFile())));
            }
        } catch (final IndexOutOfBoundsException ex) {
            Logger.getGlobal().warning("No scenario-file");
        }
    }
}
