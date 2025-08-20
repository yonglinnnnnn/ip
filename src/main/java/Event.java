public class Event extends Deadline{
    String startDateTime;

    public Event(String task, String startDT, String ddl) {
        super(task, ddl);
        this.startDateTime = startDT;
    }

    @Override
    public String toString() {
        return "[E]" + this.getStatusIcon() + " " + this.task + " (from: " + this.startDateTime + " to: " + this.deadline + ")";
    }
}
