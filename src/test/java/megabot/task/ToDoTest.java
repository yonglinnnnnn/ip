package megabot.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToDoTest {

    @Test
    void constructor_validTodo_createsCorrectly() {
        ToDo todo = new ToDo("read book");
        assertEquals("read book", todo.getTask());
        assertFalse(todo.getIsDone());
    }

    @Test
    void toString_unmarkedTodo_returnsCorrectFormat() {
        ToDo todo = new ToDo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    void toString_markedTodo_returnsCorrectFormat() {
        ToDo todo = new ToDo("read book");
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }

    @Test
    void formatData_unmarkedTodo_returnsCorrectFormat() {
        ToDo todo = new ToDo("read book");
        assertEquals("T | 0 | read book", todo.formatData());
    }

    @Test
    void formatData_markedTodo_returnsCorrectFormat() {
        ToDo todo = new ToDo("read book");
        todo.markAsDone();
        assertEquals("T | 1 | read book", todo.formatData());
    }

    @Test
    void constructor_emptyTodo_createsCorrectly() {
        ToDo todo = new ToDo("");
        assertEquals("", todo.getTask());
        assertEquals("[T][ ] ", todo.toString());
    }

    @Test
    void markAsDone_todo_worksCorrectly() {
        ToDo todo = new ToDo("test");
        todo.markAsDone();
        assertTrue(todo.getIsDone());
        assertEquals("[T][X] test", todo.toString());
    }
}