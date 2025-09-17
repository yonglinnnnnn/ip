package megabot.parser;

import megabot.task.Command;
import megabot.exception.MegabotException;

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
     * @throws MegabotException if no task number is provided or if it's not a valid integer
     */
    public static int parseTaskNumber(String userInput) throws MegabotException {
        String[] str = userInput.split(" ");
        if (str.length < 2) {
            throw new MegabotException("OOPSIE!! Please specify a task number.");
        }

        try {
            return Integer.parseInt(str[1]);
        } catch (NumberFormatException e) {
            throw new MegabotException("OOPSIE!! Please provide a valid task number.");
        }
    }

    /**
     * Parses deadline command parameters.
     * Expected format: "description /by date"
     *
     * @param task the task portion of the deadline command
     * @return a String array where [0] is description and [1] is the deadline
     * @throws MegabotException if the format is incorrect or components are empty
     */
    public static String[] parseDeadline(String task) throws MegabotException {
        String[] deadlineParts = task.split(" /(?:by) ");
        boolean isValidDeadline = true;
        boolean isLength2 = deadlineParts.length == 2;

        if (isLength2) {
            isValidDeadline = deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty();
        }

        if (!isLength2 && isValidDeadline) {
            throw new MegabotException("OOPSIE!! Please use format: deadline <task> /by <date>");
        }

        return deadlineParts;
    }

    /**
     * Parses event command parameters.
     * Expected format: "description /from start /to end"
     *
     * @param task the task portion of the event command
     * @return a String array where [0] is description, [1] is start, and [2] is end
     * @throws MegabotException if the format is incorrect or components are empty
     */
    public static String[] parseEvent(String task) throws MegabotException {
        if (task.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! The description of an event cannot be empty.");
        }

        String[] eventParts = task.split(" /(?:from|to) ");
        boolean isLength3 = eventParts.length == 3;
        boolean isValidEvent = true;

        if (isLength3) {
            isValidEvent = eventParts[0].trim().isEmpty() || eventParts[1].trim().isEmpty()
                    || eventParts[2].trim().isEmpty();
        }

        if (!isLength3 && isValidEvent) {
            throw new MegabotException("OOPSIE!! Please use format: event <task> /from <start> /to <end>");
        }

        return eventParts;
    }

    /**
     * Parses the keyword from a find command.
     * Expected format: "find keyword"
     *
     * @param userInput the user input containing the find command and keyword
     * @return the keyword to search for
     * @throws MegabotException if no keyword is provided
     */
    public static String parseFindKeyword(String userInput) throws MegabotException {
        String keyword = removeFirstWord(userInput);
        if (keyword.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Please specify a keyword to search for.");
        }
        return keyword.trim();
    }
}
