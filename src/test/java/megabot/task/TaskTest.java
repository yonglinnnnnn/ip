package megabot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void constructor_validTask_createsTaskCorrectly() {
        Task task = new Task("test task");
        assertEquals("test task", task.getTask());
        assertFalse(task.getIsDone());
        assertEquals("[ ]", task.getStatusIcon());
    }

    @Test
    void markAsDone_unmarkedTask_marksTaskAsDone() {
        Task task = new Task("test task");
        task.markAsDone();
        assertTrue(task.getIsDone());
        assertEquals("[X]", task.getStatusIcon());
    }

    @Test
    void markAsUndone_markedTask_marksTaskAsUndone() {
        Task task = new Task("test task");
        task.markAsDone();
        task.markAsUndone();
        assertFalse(task.getIsDone());
        assertEquals("[ ]", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_unmarkedTask_returnsEmptyBrackets() {
        Task task = new Task("test task");
        assertEquals("[ ]", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_markedTask_returnsXBrackets() {
        Task task = new Task("test task");
        task.markAsDone();
        assertEquals("[X]", task.getStatusIcon());
    }

    @Test
    void toString_unmarkedTask_returnsCorrectFormat() {
        Task task = new Task("test task");
        assertEquals("[ ] test task", task.toString());
    }

    @Test
    void toString_markedTask_returnsCorrectFormat() {
        Task task = new Task("test task");
        task.markAsDone();
        assertEquals("[X] test task", task.toString());
    }

    @Test
    void formatData_unmarkedTask_returnsCorrectFormat() {
        Task task = new Task("test task");
        assertEquals("0 | test task", task.formatData());
    }

    @Test
    void formatData_markedTask_returnsCorrectFormat() {
        Task task = new Task("test task");
        task.markAsDone();
        assertEquals("1 | test task", task.formatData());
    }

    @Test
    void constructor_emptyTask_createsTaskCorrectly() {
        Task task = new Task("");
        assertEquals("", task.getTask());
        assertFalse(task.getIsDone());
    }
}
