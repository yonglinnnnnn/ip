package megabot;

import megabot.exception.InvalidTaskException;

public class Parser {

    public static Command parseCommand(String userInput) {

        return Command.fromString(userInput);
    }

    public static String removeFirstWord(String str) {
        int index = str.indexOf(' ');
        if (index == -1) {
            return "";
        }
        return str.substring(index + 1);
    }

    public static int parseTaskNumber(String userInput) throws InvalidTaskException {
        String[] str = userInput.split(" ");
        if (str.length < 2) {
            throw new InvalidTaskException("OOPSIE!! Please specify a task number.");
        }

        try {
            return Integer.parseInt(str[1]);
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("OOPSIE!! Please provide a valid task number.");
        }
    }

    public static String[] parseDeadline(String task) throws InvalidTaskException {
        String[] parts = task.split(" /(?:by) ");
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! Please use format: deadline <task> /by <date>");
        }

        return parts;
    }

    public static String[] parseEvent(String task) throws InvalidTaskException {
        if (task.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of an event cannot be empty.");
        }

        String[] parts = task.split(" /(?:from|to) ");
        if (parts.length != 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! Please use format: event <task> /from <start> /to <end>");
        }

        return parts;
    }

    /**
     * Parses the keyword from a find command.
     * Expected format: "find keyword"
     *
     * @param userInput the user input containing the find command and keyword
     * @return the keyword to search for
     * @throws InvalidTaskException if no keyword is provided
     */
    public static String parseFindKeyword(String userInput) throws InvalidTaskException {
        String keyword = removeFirstWord(userInput);
        if (keyword.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! Please specify a keyword to search for.");
        }
        return keyword.trim();
    }
}