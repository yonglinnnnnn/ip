package megabot.task;

import megabot.exception.InvalidTaskException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Deadline{
    private final String stringStartDateTime;
    private final LocalDate startDateTime;

    public Event(String task, String startDT, String ddl) throws InvalidTaskException {
        super(task, ddl);
        this.stringStartDateTime = startDT;

        try {
            this.startDateTime = LocalDate.parse(startDT, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeException e) {
            throw new InvalidTaskException("OOPSIE!! The start date format is invalid. Please use YYYY-MM-DD format.");
        }
    }

    public String getStringStartDateTime() {
        return this.stringStartDateTime;
    }

    public LocalDate getStartDateTime() {
        return this.startDateTime;
    }

    public String getTaskDuration() {
        return this.startDateTime + "-" + super.getDeadline();
    }

    @Override
    public String toString() {
        String startDTFormat = this.getStartDateTime().format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        String endDTFormat = super.getDeadline().format(DateTimeFormatter.ofPattern("MMM d yyyy"));

        return "[E]" + this.getStatusIcon() + " " + super.getTask() + " (from: "
                + startDTFormat + " to: " + endDTFormat + ")";
    }

    @Override
    public String formatData() {
        String str = (super.getIsDone()) ? "1" : "0" + " | " + getTask();
        return "E | " + str + " | " + getTaskDuration();
    }
}
