package flashcards;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, String> arguments = new LinkedHashMap<>();

        for (int i = 0, j = 1; j < args.length; i+=2, j+=2) {
            arguments.put(args[i], args[j]);
        }

        FlashCard game = new FlashCard();

        if (arguments.containsKey("-import")) {
            game.setImportFile(arguments.get("-import"));
        }
        if (arguments.containsKey("-export")) {
            game.setExportFile(arguments.get("-export"));
        }

        game.run();
    }
}
