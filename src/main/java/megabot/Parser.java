package megabot;

import megabot.exception.InvalidTaskException;

/**
 * Parses user input and extracts meaningful components for command execution.
 * Provides utility methods to parse commands, task numbers, and task parameters.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Parser {

    /**
     * Parses the user input to determine the command type.
     *
     * @param userInput the raw user input string
     * @return the Command enum corresponding to the user input
     */
    public static Command parseCommand(String userInput) {
        Command result = Command.fromString(userInput);

        // Post-condition: result should never be null
        assert result != null : "parseCommand should never return null";
        return result;
    }

    /**
     * Removes the first word from a string and returns the remainder.
     * Used to extract command arguments from user input.
     *
     * @param str the input string
     * @return the string with the first word removed, or empty string if no space found
     */
    public static String removeFirstWord(String str) {
        assert str != null : "Input string cannot be null";
        int index = str.indexOf(' ');
        if (index == -1) {
            return "";
        }
        return str.substring(index + 1);
    }

    /**
     * Parses and extracts a task number from user input.
     * Expected format: "command taskNumber"
     *
     * @param userInput the user input containing a command and task number
     * @return the parsed task number
     * @throws InvalidTaskException if no task number is provided or if it's not a valid integer
     */
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

    /**
     * Parses deadline command parameters.
     * Expected format: "description /by date"
     *
     * @param task the task portion of the deadline command
     * @return a String array where [0] is description and [1] is the deadline
     * @throws InvalidTaskException if the format is incorrect or components are empty
     */
    public static String[] parseDeadline(String task) throws InvalidTaskException {
        String[] parts = task.split(" /(?:by) ");
        boolean lengthMoreThan2 = parts.length != 2;
        boolean isValidDeadline = parts[0].trim().isEmpty() || parts[1].trim().isEmpty();
        if (lengthMoreThan2 && isValidDeadline) {
            throw new InvalidTaskException("OOPSIE!! Please use format: deadline <task> /by <date>");
        }

        return parts;
    }

    /**
     * Parses event command parameters.
     * Expected format: "description /from start /to end"
     *
     * @param task the task portion of the event command
     * @return a String array where [0] is description, [1] is start, and [2] is end
     * @throws InvalidTaskException if the format is incorrect or components are empty
     */
    public static String[] parseEvent(String task) throws InvalidTaskException {
        if (task.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of an event cannot be empty.");
        }

        String[] parts = task.split(" /(?:from|to) ");
        boolean lengthMoreThan3 = parts.length != 3;
        boolean isValidEvent = parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty();
        if (lengthMoreThan3 && isValidEvent) {
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
