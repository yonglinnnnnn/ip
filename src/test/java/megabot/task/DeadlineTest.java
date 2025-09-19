package megabot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import megabot.exception.MegabotException;


class DeadlineTest {

    @Test
    void constructor_validDateTimeFormat_createsDeadlineCorrectly() throws MegabotException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");

        assertEquals("submit assignment", deadline.getTask());
        assertEquals("2023-12-01 1400", deadline.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 1, 14, 0), deadline.getDeadline());
    }

    @Test
    void constructor_validDateOnlyFormat_createsDeadlineCorrectly() throws MegabotException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01");

        assertEquals("submit assignment", deadline.getTask());
        assertEquals("2023-12-01", deadline.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 1, 0, 0), deadline.getDeadline());
    }

    @Test
    void constructor_flexibleDateFormats_createsDeadlineCorrectly() throws MegabotException {
        // Test various date formats
        Deadline deadline1 = new Deadline("task1", "25/12/2023");
        assertEquals("25/12/2023", deadline1.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), deadline1.getDeadline());

        Deadline deadline2 = new Deadline("task2", "12/25/2023");
        assertEquals("12/25/2023", deadline2.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), deadline2.getDeadline());

        Deadline deadline3 = new Deadline("task3", "25 Dec 2023");
        assertEquals("25 Dec 2023", deadline3.getStringDeadline());
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), deadline3.getDeadline());
    }

    @Test
    void constructor_invalidDateFormat_throwsException() {
        MegabotException exception = assertThrows(MegabotException.class, () ->
                new Deadline("submit assignment", "invalid date"));
        assertTrue(exception.getMessage().contains("Error parsing deadline"));
        assertTrue(exception.getMessage().contains("Invalid date/time format"));

        exception = assertThrows(MegabotException.class, () ->
                new Deadline("submit assignment", "2023-13-01"));
        assertTrue(exception.getMessage().contains("Error parsing deadline"));

        exception = assertThrows(MegabotException.class, () ->
                new Deadline("submit assignment", "32/12/2023"));
        assertTrue(exception.getMessage().contains("Error parsing deadline"));
    }

    @Test
    void constructor_emptyOrNullDeadline_throwsException() {
        MegabotException exception = assertThrows(MegabotException.class, () ->
                new Deadline("task", ""));
        assertTrue(exception.getMessage().contains("Deadline cannot be empty"));

        exception = assertThrows(MegabotException.class, () ->
                new Deadline("task", null));
        assertTrue(exception.getMessage().contains("Deadline cannot be empty"));

        exception = assertThrows(MegabotException.class, () ->
                new Deadline("task", "   "));
        assertTrue(exception.getMessage().contains("Deadline cannot be empty"));
    }

    @Test
    void toString_unmarkedDeadline_returnsCorrectFormat() throws MegabotException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        String result = deadline.toString();

        assertTrue(result.contains("[D][ ]"));
        assertTrue(result.contains("submit assignment"));
        assertTrue(result.contains("(by: Dec 1 2023 2:00 pm)"));
        assertTrue(result.contains("⚠ OVERDUE")); // Status indicator
    }

    @Test
    void toString_markedDeadline_returnsCorrectFormat() throws MegabotException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        deadline.markAsDone();
        String result = deadline.toString();

        assertTrue(result.contains("[D][X]"));
        assertTrue(result.contains("submit assignment"));
        assertTrue(result.contains("(by: Dec 1 2023 2:00 pm)"));
        assertTrue(result.contains("✓ Completed")); // Status indicator
    }

    @Test
    void formatData_unmarkedDeadline_returnsCorrectFormat() throws MegabotException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        assertEquals("D | 0 | submit assignment | 2023-12-01 1400", deadline.formatData());
    }

    @Test
    void formatData_markedDeadline_returnsCorrectFormat() throws MegabotException {
        Deadline deadline = new Deadline("submit assignment", "2023-12-01 1400");
        deadline.markAsDone();
        assertEquals("D | 1 | submit assignment | 2023-12-01 1400", deadline.formatData());
    }

    @Test
    void getDeadline_returnsCorrectLocalDateTime() throws MegabotException {
        Deadline deadline = new Deadline("test task", "2023-12-25 0900");
        LocalDateTime expected = LocalDateTime.of(2023, 12, 25, 9, 0);
        assertEquals(expected, deadline.getDeadline());
    }

    @Test
    void getOriginalDeadlineInput_returnsOriginalString() throws MegabotException {
        String dateString = "2023-12-01 1400";
        Deadline deadline = new Deadline("test task", dateString);
        assertEquals(dateString, deadline.getStringDeadline());

        // Test that original format is preserved even with flexible parsing
        String flexibleFormat = "25/12/2023";
        Deadline deadline2 = new Deadline("test task", flexibleFormat);
        assertEquals(flexibleFormat, deadline2.getStringDeadline());
    }

    @Test
    void isOverdue_pastDeadline_returnsTrue() throws MegabotException {
        // Create a deadline in the past
        Deadline deadline = new Deadline("past task", "2020-01-01");
        assertTrue(deadline.isOverdue());
    }

    @Test
    void isOverdue_futureDeadline_returnsFalse() throws MegabotException {
        // Create a deadline in the future
        Deadline deadline = new Deadline("future task", "2030-12-31");
        assertFalse(deadline.isOverdue());
    }

    @Test
    void getDeadlineStatus_completedTask_returnsCompleted() throws MegabotException {
        Deadline deadline = new Deadline("test task", "2023-12-01");
        deadline.markAsDone();
        assertEquals("✓ Completed", deadline.getDeadlineStatus());
    }

    @Test
    void getDeadlineStatus_overdueTask_returnsOverdue() throws MegabotException {
        Deadline deadline = new Deadline("test task", "2020-01-01");
        assertEquals("⚠ OVERDUE", deadline.getDeadlineStatus());
    }

    @Test
    void getDeadlineStatus_upcomingTask_returnsUpcoming() throws MegabotException {
        Deadline deadline = new Deadline("test task", "2030-12-31");
        assertEquals("📅 Upcoming", deadline.getDeadlineStatus());
    }

    @Test
    void markAsDone_deadline_worksCorrectly() throws MegabotException {
        Deadline deadline = new Deadline("test task", "2023-12-01");
        assertFalse(deadline.getIsDone());

        deadline.markAsDone();
        assertTrue(deadline.getIsDone());
        assertTrue(deadline.toString().contains("[D][X]"));
        assertEquals("✓ Completed", deadline.getDeadlineStatus());
    }

    @Test
    void markAsUndone_deadline_worksCorrectly() throws MegabotException {
        Deadline deadline = new Deadline("test task", "2023-12-01");
        deadline.markAsDone();
        assertTrue(deadline.getIsDone());

        deadline.markAsUndone();
        assertFalse(deadline.getIsDone());
        assertTrue(deadline.toString().contains("[D][ ]"));
    }

    @Test
    void constructor_trimmedInputs_handlesWhitespaceCorrectly() throws MegabotException {
        Deadline deadline = new Deadline("  submit assignment  ", "  2023-12-01 1400  ");

        assertEquals("  submit assignment  ", deadline.getTask()); // Task preserves original spacing
        assertEquals("2023-12-01 1400", deadline.getStringDeadline()); // Date is trimmed
        assertEquals(LocalDateTime.of(2023, 12, 1, 14, 0), deadline.getDeadline());
    }
}
