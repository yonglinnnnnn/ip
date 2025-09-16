package megabot.task;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import megabot.exception.MegabotException;

/**
 * Represents a task that occurs during a specific time period.
 * An event task extends Deadline and has both a start date and an end date.
 * The start date format should be "yyyy-MM-dd" and end date follows the Deadline format.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Event extends Deadline {
    private final String stringStartDateTime;
    private final LocalDate startDateTime;

    /**
     * Constructs an Event task with the specified description, start date, and end date.
     *
     * @param task the description of the event task
     * @param startDT the start date in format "yyyy-MM-dd"
     * @param ddl the end date in format "yyyy-MM-dd" or "yyyy-MM-dd HHmm"
     * @throws MegabotException if either date format is invalid
     */
    public Event(String task, String startDT, String ddl) throws MegabotException {
        super(task, ddl);
        this.stringStartDateTime = startDT;

        try {
            this.startDateTime = LocalDate.parse(startDT, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeException e) {
            throw new MegabotException("OOPSIE!! The start date format is invalid. Please use YYYY-MM-DD format.");
        }
    }

    /**
     * Returns the original start date string as provided by the user.
     *
     * @return the original start date string
     */
    public String getStringStartDateTime() {
        return this.stringStartDateTime;
    }

    /**
     * Returns the parsed start date as a LocalDate object.
     *
     * @return the start date as LocalDate
     */
    public LocalDate getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Returns the task duration string combining start and end dates.
     * Format: "startDate-endDateTime"
     *
     * @return the duration string for file storage
     */
    public String getTaskDuration() {
        return this.startDateTime + "-" + super.getDeadline();
    }

    @Override
    public String toString() {
        String startDatetimeFormat = this.getStartDateTime().format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        String endDatetimeFormat = super.getDeadline().format(DateTimeFormatter.ofPattern("MMM d yyyy"));

        return "[E]" + this.getStatusIcon() + " " + super.getTask() + " (from: "
                + startDatetimeFormat + " to: " + endDatetimeFormat + ")";
    }

    /**
     * Returns the event task data in a format suitable for file storage.
     * Format: "E | STATUS | DESCRIPTION | DURATION"
     *
     * @return formatted string for file storage
     */
    @Override
    public String formatData() {
        String str = (super.getIsDone()) ? "1" : "0" + " | " + getTask();
        return "E | " + str + " | " + getTaskDuration();
    }
}
