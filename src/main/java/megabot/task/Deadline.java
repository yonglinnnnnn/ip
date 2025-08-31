package megabot.task;

import megabot.exception.InvalidTaskException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 * A deadline task has a description and a specific date/time by which it should be completed.
 * Supports two date formats: "yyyy-MM-dd" (date only) and "yyyy-MM-dd HHmm" (date with time).
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Deadline extends Task {
    private final String stringDeadline;
    private final LocalDateTime deadline;

    /**
     * Constructs a Deadline task with the specified description and deadline.
     *
     * @param task the description of the deadline task
     * @param ddl the deadline in format "yyyy-MM-dd" or "yyyy-MM-dd HHmm"
     * @throws InvalidTaskException if the deadline format is invalid
     */
    public Deadline(String task, String ddl) throws InvalidTaskException {
        super(task);
        this.stringDeadline = ddl;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime parsedDueDate;

        try {
            parsedDueDate = LocalDateTime.parse(ddl, dateTimeFormatter);
        } catch (DateTimeException e) {
            try {
                parsedDueDate = LocalDateTime.of(LocalDate.parse(ddl, dateFormatter), LocalTime.MIDNIGHT);
            } catch (DateTimeParseException e2) {
                throw new InvalidTaskException("OOPSIE!! The deadline format is invalid. Please use YYYY-MM-DD or YYYY-MM-DD HHMM format.");
            }
        }

        this.deadline = parsedDueDate;
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

    @Override
    public String toString() {
        return "[D]" + this.getStatusIcon() + " " + super.getTask() + " (by: "
                + this.getDeadline().format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm")) + ")";
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
