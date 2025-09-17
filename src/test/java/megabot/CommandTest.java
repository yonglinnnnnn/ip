package megabot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import megabot.task.Command;

class CommandTest {

    @Test
    void getCommandText_allCommands_returnCorrectText() {
        assertEquals("list", Command.LIST.getCommandText());
        assertEquals("todo", Command.TODO.getCommandText());
        assertEquals("deadline", Command.DEADLINE.getCommandText());
        assertEquals("event", Command.EVENT.getCommandText());
        assertEquals("mark", Command.MARK.getCommandText());
        assertEquals("unmark", Command.UNMARK.getCommandText());
        assertEquals("delete", Command.DELETE.getCommandText());
        assertEquals("bye", Command.BYE.getCommandText());
        assertEquals("", Command.UNKNOWN.getCommandText());
    }

    @Test
    void fromString_exactMatch_returnsCorrectCommand() {
        assertEquals(Command.LIST, Command.fromString("list"));
        assertEquals(Command.BYE, Command.fromString("bye"));
        assertEquals(Command.TODO, Command.fromString("todo"));
        assertEquals(Command.DEADLINE, Command.fromString("deadline"));
        assertEquals(Command.EVENT, Command.fromString("event"));
        assertEquals(Command.MARK, Command.fromString("mark"));
        assertEquals(Command.UNMARK, Command.fromString("unmark"));
        assertEquals(Command.DELETE, Command.fromString("delete"));
    }

    @Test
    void fromString_caseInsensitive_returnsCorrectCommand() {
        assertEquals(Command.LIST, Command.fromString("LIST"));
        assertEquals(Command.BYE, Command.fromString("BYE"));
        assertEquals(Command.TODO, Command.fromString("TODO"));
        assertEquals(Command.DEADLINE, Command.fromString("DEADLINE"));
    }

    @Test
    void fromString_withArguments_returnsCorrectCommand() {
        assertEquals(Command.TODO, Command.fromString("todo read book"));
        assertEquals(Command.DEADLINE, Command.fromString("deadline submit /by Sunday"));
        assertEquals(Command.EVENT, Command.fromString("event meeting /from Mon /to Tue"));
        assertEquals(Command.MARK, Command.fromString("mark 1"));
        assertEquals(Command.UNMARK, Command.fromString("unmark 2"));
        assertEquals(Command.DELETE, Command.fromString("delete 3"));
    }

    @Test
    void fromString_withWhitespace_returnsCorrectCommand() {
        assertEquals(Command.LIST, Command.fromString(" list "));
        assertEquals(Command.BYE, Command.fromString(" bye "));
        assertEquals(Command.TODO, Command.fromString(" todo read book "));
    }

    @Test
    void fromString_invalidInput_returnsUnknown() {
        assertEquals(Command.UNKNOWN, Command.fromString("invalid"));
        assertEquals(Command.UNKNOWN, Command.fromString("xyz"));
        assertEquals(Command.UNKNOWN, Command.fromString("123"));
        assertEquals(Command.UNKNOWN, Command.fromString(""));
        assertEquals(Command.UNKNOWN, Command.fromString(null));
        assertEquals(Command.UNKNOWN, Command.fromString("   "));
    }

    @Test
    void fromString_listCommandWithArguments_returnsUnknown() {
        // list and bye commands should not accept arguments
        assertEquals(Command.UNKNOWN, Command.fromString("list something"));
        assertEquals(Command.UNKNOWN, Command.fromString("bye something"));
    }

    @Test
    void fromString_partialMatch_returnsUnknown() {
        assertEquals(Command.UNKNOWN, Command.fromString("tod"));
        assertEquals(Command.UNKNOWN, Command.fromString("deadlin"));
        assertEquals(Command.UNKNOWN, Command.fromString("lis"));
    }
}
