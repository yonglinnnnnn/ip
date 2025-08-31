package megabot;

public enum Command {
    LIST("list"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    BYE("bye"),
    UNKNOWN("");

    private final String commandText;

    Command(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return this.commandText;
    }

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

    private static Command fromExactMatch(String input) {
        for (Command cmd: Command.values()) {
            if (input.equals(cmd.commandText)) {
                return cmd;
            }
        }

        return UNKNOWN;
    }
}
