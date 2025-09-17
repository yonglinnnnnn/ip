package megabot.task;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import megabot.exception.MegabotException;
import megabot.parser.DateTimeParser;

/**
 * Represents a task with a deadline.
 * A deadline task has a description and a specific date/time by which it should be completed.
 * Supports two date formats: "yyyy-MM-dd" (date only) and "yyyy-MM-dd HHmm" (date with time).
 *
 * @author Xu Yong Lin
 * @version 2.0
 */
public class Deadline extends Task {
    private final String stringDeadline;
    private final LocalDateTime deadline;

    /**
     * Constructs a Deadline task with the specified description and deadline.
     *
     * @param task the description of the deadline task
     * @param deadlineStr the deadline in format "yyyy-MM-dd" or "yyyy-MM-dd HHmm"
     * @throws MegabotException if the deadline format is invalid
     */
    public Deadline(String task, String deadlineStr) throws MegabotException {
        super(task);

        // Validate inputs
        if (task == null || task.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Task description cannot be empty.");
        }

        if (deadlineStr == null || deadlineStr.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Deadline cannot be empty.");
        }

        this.stringDeadline = deadlineStr.trim();

        try {
            this.deadline = DateTimeParser.parseDateTime(deadlineStr);
        } catch (MegabotException e) {
            // Re-throw with context about which field failed
            throw new MegabotException("OOPSIE!! Error parsing deadline: " + e.getMessage());
        }

        //        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        //        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //
        //        LocalDateTime parsedDueDate;
        //
        //        try {
        //            parsedDueDate = LocalDateTime.parse(ddl, dateTimeFormatter);
        //        } catch (DateTimeException e) {
        //            try {
        //                parsedDueDate = LocalDateTime.of(LocalDate.parse(ddl, dateFormatter), LocalTime.MIDNIGHT);
        //            } catch (DateTimeParseException e2) {
        //                throw new MegabotException("OOPSIE!! The deadline format is invalid. "
        //                        + "Please use YYYY-MM-DD or YYYY-MM-DD HHMM format.");
        //            }
        //        }
    }

    /**
     * Returns the original deadline string as provided by the user.
     *
     * @return the original deadline string
     */
    public String getStringDeadline() {
        return this.stringDeadline;
    }

    /**
     * Returns the parsed deadline as a LocalDateTime object.
     *
     * @return the deadline as LocalDateTime
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    /**
     * Checks if the deadline has passed.
     *
     * @return true if the deadline is in the past
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(this.deadline);
    }

    /**
     * Gets a user-friendly status message about the deadline.
     *
     * @return status message indicating if task is overdue, due soon, etc.
     */
    public String getDeadlineStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (getIsDone()) {
            return "✓ Completed";
        } else if (now.isAfter(deadline)) {
            return "⚠ OVERDUE";
        } else if (now.plusDays(1).isAfter(deadline)) {
            return "⏰ Due soon";
        } else {
            return "📅 Upcoming";
        }
    }

    @Override
    public String toString() {
        String statusEmoji = getDeadlineStatus();
        return "[D]" + this.getStatusIcon() + " " + super.getTask() + " (by: "
                + this.getDeadline().format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm"))
                + ") " + statusEmoji;
    }

    /**
     * Returns the deadline task data in a format suitable for file storage.
     * Format: "D | STATUS | DESCRIPTION | DEADLINE"
     *
     * @return formatted string for file storage
     */
    @Override
    public String formatData() {
        return "D | " + super.formatData() + " | " + this.getStringDeadline();
    }
}
