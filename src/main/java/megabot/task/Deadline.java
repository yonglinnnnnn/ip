package megabot.task;

import megabot.exception.InvalidTaskException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private final String stringDeadline;
    private final LocalDateTime deadline;

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

    public String getStringDeadline() {
        return this.stringDeadline;
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D]" + this.getStatusIcon() + " " + this.task + " (by: "
                + this.getDeadline().format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm")) + ")";
    }

    @Override
    public String formatData() {
        return "D | " + super.formatData() + " | " + this.getStringDeadline();
    }
}
