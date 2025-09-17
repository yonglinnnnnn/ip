package megabot.task;

/**
 * Enumeration of all possible commands that can be executed in the MegaBot application.
 * Each command has an associated text representation that users can type.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public enum Command {
    LIST("list"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    FIND("find"),
    BYE("bye"),
    UNKNOWN("");

    private final String commandText;

    /**
     * Constructs a Command enum with the associated command text.
     *
     * @param commandText the text representation of the command
     */
    Command(String commandText) {
        this.commandText = commandText;
    }

    /**
     * Returns the text representation of the command.
     *
     * @return the command text
     */
    public String getCommandText() {
        return this.commandText;
    }

    /**
     * Parses a user input string and returns the corresponding Command.
     * Handles case-insensitive matching and commands with arguments.
     *
     * @param input the user input string to parse
     * @return the corresponding Command enum, or UNKNOWN if no match is found
     */
    public static Command fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String lowerInput = input.toLowerCase().trim();

        // Check for exact match first (for commands like "list", "bye")
        if (lowerInput.equals("list") || lowerInput.equals("bye")) {
            return fromExactMatch(lowerInput);
        }

        // Check if input starts with command word followed by space
        for (Command cmd : Command.values()) {
            if (cmd != UNKNOWN && cmd != LIST && cmd != BYE) {
                if (lowerInput.startsWith(cmd.commandText + " ") || lowerInput.equals(cmd.commandText)) {
                    return cmd;
                }
            }
        }

        return UNKNOWN;
    }

    /**
     * Helper method to find exact command matches.
     *
     * @param input the normalized input string
     * @return the matching Command or UNKNOWN if no exact match is found
     */
    private static Command fromExactMatch(String input) {
        for (Command cmd: Command.values()) {
            if (input.equals(cmd.commandText)) {
                return cmd;
            }
        }

        return UNKNOWN;
    }
}
