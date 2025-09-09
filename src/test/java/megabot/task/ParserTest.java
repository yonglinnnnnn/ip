package megabot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import megabot.Command;
import megabot.Parser;
import megabot.exception.InvalidTaskException;

class ParserTest {

    @Test
    void parseCommand_todoCommand_returnsTodoCommand() {
        assertEquals(Command.TODO, Parser.parseCommand("todo read book"));
        assertEquals(Command.TODO, Parser.parseCommand("TODO READ BOOK"));
        assertEquals(Command.TODO, Parser.parseCommand("todo"));
    }

    @Test
    void parseCommand_listCommand_returnsListCommand() {
        assertEquals(Command.LIST, Parser.parseCommand("list"));
        assertEquals(Command.LIST, Parser.parseCommand("LIST"));
        assertEquals(Command.LIST, Parser.parseCommand(" list "));
    }

    @Test
    void parseCommand_deadlineCommand_returnsDeadlineCommand() {
        assertEquals(Command.DEADLINE, Parser.parseCommand("deadline submit assignment /by Sunday"));
        assertEquals(Command.DEADLINE, Parser.parseCommand("deadline"));
    }

    @Test
    void parseCommand_eventCommand_returnsEventCommand() {
        assertEquals(Command.EVENT, Parser.parseCommand("event project meeting /from Mon /to Tue"));
        assertEquals(Command.EVENT, Parser.parseCommand("event"));
    }

    @Test
    void parseCommand_markCommand_returnsMarkCommand() {
        assertEquals(Command.MARK, Parser.parseCommand("mark 1"));
        assertEquals(Command.MARK, Parser.parseCommand("mark"));
    }

    @Test
    void parseCommand_unmarkCommand_returnsUnmarkCommand() {
        assertEquals(Command.UNMARK, Parser.parseCommand("unmark 1"));
        assertEquals(Command.UNMARK, Parser.parseCommand("unmark"));
    }

    @Test
    void parseCommand_deleteCommand_returnsDeleteCommand() {
        assertEquals(Command.DELETE, Parser.parseCommand("delete 1"));
        assertEquals(Command.DELETE, Parser.parseCommand("delete"));
    }

    @Test
    void parseCommand_byeCommand_returnsByeCommand() {
        assertEquals(Command.BYE, Parser.parseCommand("bye"));
        assertEquals(Command.BYE, Parser.parseCommand("BYE"));
    }

    @Test
    void parseCommand_unknownCommand_returnsUnknownCommand() {
        assertEquals(Command.UNKNOWN, Parser.parseCommand("unknown"));
        assertEquals(Command.UNKNOWN, Parser.parseCommand("xyz"));
        assertEquals(Command.UNKNOWN, Parser.parseCommand(""));
        assertEquals(Command.UNKNOWN, Parser.parseCommand(null));
    }

    @Test
    void removeFirstWord_normalString_returnsRestOfString() {
        assertEquals("read book", Parser.removeFirstWord("todo read book"));
        assertEquals("submit assignment /by Sunday", Parser.removeFirstWord("deadline submit assignment /by Sunday"));
        assertEquals("1", Parser.removeFirstWord("mark 1"));
    }

    @Test
    void removeFirstWord_singleWord_returnsEmptyString() {
        assertEquals("", Parser.removeFirstWord("todo"));
        assertEquals("", Parser.removeFirstWord("list"));
        assertEquals("", Parser.removeFirstWord("bye"));
    }

    @Test
    void removeFirstWord_emptyString_returnsEmptyString() {
        assertEquals("", Parser.removeFirstWord(""));
    }

    @Test
    void parseTaskNumber_validInput_returnsCorrectNumber() throws InvalidTaskException {
        assertEquals(1, Parser.parseTaskNumber("mark 1"));
        assertEquals(5, Parser.parseTaskNumber("delete 5"));
        assertEquals(10, Parser.parseTaskNumber("unmark 10"));
    }

    @Test
    void parseTaskNumber_invalidInput_throwsException() {
        assertThrows(InvalidTaskException.class, () -> Parser.parseTaskNumber("mark"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseTaskNumber("delete"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseTaskNumber("mark abc"));
    }

    @Test
    void parseDeadline_validInput_returnsCorrectParts() throws InvalidTaskException {
        String[] result = Parser.parseDeadline("submit assignment /by Sunday");
        assertEquals(2, result.length);
        assertEquals("submit assignment", result[0]);
        assertEquals("Sunday", result[1]);
    }

    @Test
    void parseDeadline_invalidInput_throwsException() {
        assertThrows(InvalidTaskException.class, () -> Parser.parseDeadline("submit assignment"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseDeadline("/by Sunday"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseDeadline("submit assignment /by"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseDeadline(""));
    }

    @Test
    void parseEvent_validInput_returnsCorrectParts() throws InvalidTaskException {
        String[] result = Parser.parseEvent("project meeting /from Mon /to Tue");
        assertEquals(3, result.length);
        assertEquals("project meeting", result[0]);
        assertEquals("Mon", result[1]);
        assertEquals("Tue", result[2]);
    }

    @Test
    void parseEvent_invalidInput_throwsException() {
        assertThrows(InvalidTaskException.class, () -> Parser.parseEvent("project meeting"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseEvent("project meeting /from Mon"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseEvent("/from Mon /to Tue"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseEvent("project meeting /from /to Tue"));
        assertThrows(InvalidTaskException.class, () -> Parser.parseEvent(""));
    }
}
