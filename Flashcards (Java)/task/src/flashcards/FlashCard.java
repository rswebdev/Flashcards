package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class FlashCard {

    public enum InputAction {
        ADD, REMOVE, IMPORT, EXPORT, ASK, EXIT, LOG, HARDEST_CARD, RESET_STATS
    }

    private final static String savePath = "C:\\Users\\A760323\\";

    private Map<String, String> cardToDefinition = new LinkedHashMap<>();
    private Map<String, String> definitionToCard = new LinkedHashMap<>();
    private Map<String, Integer> cardStatistics = new LinkedHashMap<>();
    private ArrayList<String> appLog = new ArrayList<>();
    private String importFileName = null;
    private String exportFileName = null;

    void run() {

        if (importFileName != null) {
            try {
                importCard(importFileName);
            } catch (FileNotFoundException e) {
                // do nothing
            }
        }

        Scanner scanner = new Scanner(System.in);

        InputAction userInput = inputAction(scanner);

        while (!userInput.equals(InputAction.EXIT)) {
            switch (userInput) {
                case ADD:
                    addCard(scanner);
                    break;
                case REMOVE:
                    removeCard(scanner);
                    break;
                case ASK:
                    askCard(scanner);
                    break;
                case IMPORT:
                    importCards(scanner);
                    break;
                case EXPORT:
                    exportCards(scanner);
                    break;
                case LOG:
                    saveLog(scanner);
                    break;
                case HARDEST_CARD:
                    hardestCard();
                    break;
                case RESET_STATS:
                    resetStats();
                    break;
                default:
                    break;
            }
            printLnOut();
            userInput = inputAction(scanner);
        }
        if (exportFileName != null) {
            try {
                export(exportFileName);
            } catch (IOException e) {
                // do nothing
            }
        }
        printLnOut("Bye bye!");
    }

    void setImportFile(String filename) {
        this.importFileName = filename;
    }

    void setExportFile(String filename) {
        this.exportFileName = filename;
    }

    private String getInput(Scanner s) {
        String input = s.nextLine();
        appLog.add(input + "\n");
        return input;
    }

    private void printLnOut() {
        printLnOut("");
    }

    private void printLnOut(String output) {
        appLog.add(output + "\n");
        System.out.println(output);
    }

    private void printOut(String output) {
        String lastStringFromLog = appLog.get(appLog.size()-1);
        if (lastStringFromLog.endsWith("\n")) {
            appLog.add(output);
        } else {
            appLog.set(appLog.size() - 1, lastStringFromLog + output);
        }
        System.out.print(output);
    }

    private void addCard(Scanner s) {
        printLnOut("The card:");
        String card = getInput(s);
        boolean doAdd = true;
        if (cardToDefinition.containsKey(card)) {
            printLnOut(String.format("\"%s\" is already on another card. Try again.", card));
            //addCard(s);
            //return;
            doAdd = false;
        }

        printLnOut("The definition of the card:");
        String definition = getInput(s);

        if (definitionToCard.containsKey(definition)) {
            printLnOut(String.format("\"%s\" is already defined on another card. Try again.", card));
            //addCard(s);
            //return;
            doAdd = false;
        }
        if (doAdd) {
            definitionToCard.put(definition, card);
            cardToDefinition.put(card, definition);
            cardStatistics.put(card, 0);

            printLnOut(String.format("The pair (\"%s\":\"%s\") is added.", card, definition));
        }
    }

    private void removeCard(Scanner s) {
        printLnOut("The card:");
        String card = getInput(s);

        if (cardToDefinition.containsKey(card)) {
            String definition = cardToDefinition.get(card);
            cardToDefinition.remove(card);
            definitionToCard.remove(definition);
            cardStatistics.remove(card);
            printLnOut(String.format("Card \"%s\" removed.", card));
        } else {
            printLnOut(String.format("Can't remove \"%s\": There is no such card.", card));
        }
    }

    private void askCard(Scanner s) {
        printLnOut("How many times to ask?");
        int numberOfQuestions = Integer.parseInt(getInput(s));

        Random random = new Random();
        int from = 0;
        int to = cardToDefinition.size();

        for (int i = 0; i < numberOfQuestions; i++) {

            String[] cards = cardToDefinition.keySet().toArray(new String[0]);
            String card = cards[random.nextInt(to - from) + from];

            printLnOut(String.format("Print the definition of \"%s\":", card));
            String answer = getInput(s);
            if (cardToDefinition.get(card).equals(answer)) {
                printOut("Correct answer. ");
            } else {
                if (definitionToCard.containsKey(answer)) {
                    printOut(String.format("Wrong answer (the correct one is \"%s\", you've just written a definition of \"%s\" card). ", cardToDefinition.get(card), definitionToCard.get(answer)));
                } else {
                    printOut(String.format("Wrong answer (the correct one is \"%s\"). ", cardToDefinition.get(card)));
                }
                Integer currentStats = cardStatistics.get(card);
                cardStatistics.put(card, ++currentStats);
            }
        }
    }

    private void exportCards(Scanner s) {

        printLnOut("File name:");
        String fileName = getInput(s);

        try {
            export(fileName);
            printLnOut(String.format("%d cards have been saved", cardToDefinition.size()));
        } catch (IOException e) {
            printLnOut(String.format("%d cards have been saved", 0));
        }
    }

    private void export(String fileName) throws IOException {
        File saveFile = new File(savePath + fileName);

        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            for (Map.Entry<String, String> card : cardToDefinition.entrySet()) {
                fileWriter.write(card.getKey() + "\n");
                fileWriter.write(card.getValue() + "\n");
                fileWriter.write(cardStatistics.get(card.getKey()) + "\n");
            }
        }
    }

    private void hardestCard() {
        Integer maxErrors = 0;
        String hardestCard = "";

        for (Map.Entry<String, Integer> cardStatistic : cardStatistics.entrySet()) {
            if (cardStatistic.getValue() > maxErrors) {
                hardestCard = cardStatistic.getKey();
                maxErrors = cardStatistic.getValue();
            }
        }
        printLnOut(String.format("The hardest card is \"%s\". You have %d errors answering it.", hardestCard, maxErrors));
    }

    private void saveLog(Scanner s) {

        printLnOut("File name:");
        String fileName = getInput(s);

        File saveFile = new File(savePath + fileName);

        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            for (String logLine : appLog) {
                fileWriter.write(logLine);
            }
            printLnOut("The log has been saved.");
        } catch (IOException e) {
            printLnOut("The log has not been saved.");
        }
    }

    private void resetStats() {
        for (Map.Entry <String, String> card: cardToDefinition.entrySet()) {
            cardStatistics.put(card.getValue(), 0);
        }
        printLnOut("Statistics have been reseted");
    }

    private void importCards(Scanner s) {
        printLnOut("File name:");
        String fileName = getInput(s);

        try {
            printLnOut(String.format("%d cards have been loaded.", importCard(fileName)));
        } catch (FileNotFoundException e) {
            printLnOut(String.format("%d cards have been loaded.", 0));
        }
    }

    private int importCard(String fileName) throws FileNotFoundException {
        File saveFile = new File(savePath + fileName);

        try (Scanner fileReader = new Scanner(saveFile)) {
            boolean isDefinition = false;
            boolean isMistakes = false;
            int pairIndex = 0;
            LinkedHashMap<Integer, String> cards = new LinkedHashMap<>();
            LinkedHashMap<Integer, String> definitions = new LinkedHashMap<>();
            LinkedHashMap<Integer, Integer> mistakes = new LinkedHashMap<>();

            while (fileReader.hasNextLine()) {
                String importString = fileReader.nextLine();
                if (!isDefinition && !isMistakes) {
                    cards.put(pairIndex, importString);
                    isDefinition = true;
                } else if (isDefinition) {
                    definitions.put(pairIndex, importString);
                    isDefinition = false;
                    isMistakes = true;
                } else {
                    mistakes.put(pairIndex, Integer.parseInt(importString));
                    pairIndex++;
                    isMistakes = false;
                }
            }
            for (Map.Entry <Integer, String> entry : cards.entrySet()) {
                cardToDefinition.put(entry.getValue(), definitions.get(entry.getKey()));
                definitionToCard.put(definitions.get(entry.getKey()), entry.getValue());
                cardStatistics.put(entry.getValue(), mistakes.get(entry.getKey()));
            }

            return pairIndex;
        }
    }

    private InputAction inputAction(Scanner s) {
        StringBuilder actions = new StringBuilder();

        ArrayList<InputAction> actionsList = new ArrayList<>();

        Collections.addAll(actionsList, InputAction.values());
        Iterator actionsIterator = actionsList.iterator();

        while (actionsIterator.hasNext()) {
            InputAction action = (InputAction)actionsIterator.next();
            actions.append(action.toString().toLowerCase().replace('_', ' '));
            if (actionsIterator.hasNext()) {
                actions.append(", ");
            }
        }
        printLnOut(String.format("Input the action (%s):", actions.toString()));
        String userAction = getInput(s);
        try {
            return InputAction.valueOf(userAction.toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            printLnOut("Invalid input. Try again.");
            return inputAction(s);
        }
    }
}
