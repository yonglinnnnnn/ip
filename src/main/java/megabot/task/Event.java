package megabot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import megabot.exception.MegabotException;
import megabot.parser.DateTimeParser;

/**
 * Represents a task that occurs during a specific time period.
 * An event task extends Deadline and has both a start date and an end date.
 * The start date format should be "yyyy-MM-dd" and end date follows the Deadline format.
 *
 * @author Xu Yong Lin
 * @version 2.0
 */
public class Event extends Task {
    private final String originalStartInput;
    private final String originalEndInput;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    /**
     * Constructs an Event task with the specified description, start date, and end date.
     *
     * @param task the description of the event task
     * @param startDT the start date in format "yyyy-MM-dd"
     * @param endDT the end date in format "yyyy-MM-dd" or "yyyy-MM-dd HHmm"
     * @throws MegabotException if either date format is invalid
     */
    public Event(String task, String startDT, String endDT) throws MegabotException {
        super(task);

        // Validate inputs
        if (task == null || task.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Event description cannot be empty.");
        }

        if (startDT == null || startDT.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! Start date cannot be empty.");
        }

        if (endDT == null || endDT.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! End date cannot be empty.");
        }

        this.originalStartInput = startDT.trim();
        this.originalEndInput = endDT.trim();

        this.startDateTime = convertStringToDateTime(startDT);
        this.endDateTime = convertStringToDateTime(endDT);

        // Validate date range - start date should not be after end date
        if (this.startDateTime.isAfter(this.endDateTime)) {
            throw new MegabotException("OOPSIE!! Start date (" + this.startDateTime
                    + ") cannot be after end date (" + this.endDateTime + ").");
        }
    }

    private LocalDateTime convertStringToDateTime(String datetime) throws MegabotException {
        try {
            // Parse start date
            return DateTimeParser.parseDateTime(datetime);
        } catch (MegabotException e) {
            throw new MegabotException("Error parsing start date: " + e.getMessage());
        }
    }

    /**
     * Returns the parsed start date as a LocalDate object.
     *
     * @return the start date as LocalDate
     */
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Returns the parsed start date as a LocalDate object.
     *
     * @return the start date as LocalDate
     */
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    /**
     * Returns the task duration string combining start and end dates.
     * Format: "startDate-endDateTime"
     *
     * @return the duration string for file storage
     */
    public String getTaskDuration() {
        return this.startDateTime + "-" + this.endDateTime;
    }

    /**
     * Checks if the event is currently ongoing.
     *
     * @return true if current time is between start and end
     */
    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.startDateTime) && now.isBefore(this.endDateTime);
    }

    /**
     * Checks if the event has finished.
     *
     * @return true if the event end time has passed
     */
    public boolean hasEnded() {
        return LocalDateTime.now().isAfter(this.endDateTime);
    }

    /**
     * Gets a user-friendly status message about the event.
     *
     * @return status message indicating if event is ongoing, finished, etc.
     */
    public String getEventStatus() {
        if (getIsDone()) {
            return "✓ Completed";
        } else if (isOngoing()) {
            return "🔄 Ongoing";
        } else if (hasEnded()) {
            return "⏰ Ended";
        } else {
            return "📅 Upcoming";
        }
    }

    @Override
    public String toString() {
        String startDatetimeFormat = this.getStartDateTime().format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a"));
        String endDatetimeFormat = this.getEndDateTime().format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a"));
        String statusEmoji = getEventStatus();

        return "[E]" + this.getStatusIcon() + " " + super.getTask() + " (from: "
                + startDatetimeFormat + " to: " + endDatetimeFormat
                + ") " + statusEmoji;
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
