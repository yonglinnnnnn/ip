package megabot.task;

import megabot.exception.InvalidTaskException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DeadlineTest {

    @Test
    void constructor_validDateTimeFormat_createsDeadlineCorrectly() throws InvalidTaskException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");

        assertEquals("submit assignment", deadline.getTask());
        assertEquals("2023-12-01 1400", deadline.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 1, 14, 0), deadline.getDeadline());
    }

    @Test
    void constructor_validDateOnlyFormat_createsDeadlineCorrectly() throws InvalidTaskException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01");

        assertEquals("submit assignment", deadline.getTask());
        assertEquals("2023-12-01", deadline.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 1, 0, 0), deadline.getDeadline());
    }

    @Test
    void constructor_invalidDateFormat_throwsException() {
        assertThrows(InvalidTaskException.class, () ->
                new Deadline("submit assignment", "invalid date"));
        assertThrows(InvalidTaskException.class, () ->
                new Deadline("submit assignment", "2023-13-01"));
        assertThrows(InvalidTaskException.class, () ->
                new Deadline("submit assignment", "01-12-2023"));
    }

    @Test
    void constructor_emptyTask_createsDeadlineCorrectly() throws InvalidTaskException {
        Deadline deadline = new Deadline("", "2023-12-01");
        assertEquals("", deadline.getTask());
    }

    @Test
    void toString_unmarkedDeadline_returnsCorrectFormat() throws InvalidTaskException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        assertEquals("[D][ ] submit assignment (by: Dec 1 2023 14:00)", deadline.toString());
    }

    @Test
    void toString_markedDeadline_returnsCorrectFormat() throws InvalidTaskException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        deadline.markAsDone();
        assertEquals("[D][X] submit assignment (by: Dec 1 2023 14:00)", deadline.toString());
    }

    @Test
    void formatData_unmarkedDeadline_returnsCorrectFormat() throws InvalidTaskException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        assertEquals("D | 0 | submit assignment | 2023-12-01 1400", deadline.formatData());
    }

    @Test
    void formatData_markedDeadline_returnsCorrectFormat() throws InvalidTaskException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        deadline.markAsDone();
        assertEquals("D | 1 | submit assignment | 2023-12-01 1400", deadline.formatData());
    }

    @Test
    void getDeadline_returnsCorrectLocalDateTime() throws InvalidTaskException {
        Deadline deadline = new Deadline("test task", "2023-12-25 0900");
        LocalDateTime expected = LocalDateTime.of(2023, 12, 25, 9, 0);
        assertEquals(expected, deadline.getDeadline());
    }

    @Test
    void getStringDeadline_returnsOriginalString() throws InvalidTaskException {
        String dateString = "2023-12-01 1400";
        Deadline deadline = new Deadline("test task", dateString);
        assertEquals(dateString, deadline.getStringDeadline());
    }

    @Test
    void markAsDone_deadline_worksCorrectly() throws InvalidTaskException {
        Deadline deadline = new Deadline("test task", "2023-12-01");
        deadline.markAsDone();
        assertTrue(deadline.getIsDone());
        assertTrue(deadline.toString().contains("[D][X]"));
    }
}