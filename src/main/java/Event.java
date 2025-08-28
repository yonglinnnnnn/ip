public class Event extends Deadline{
    String startDateTime;

    public Event(String task, String startDT, String ddl) {
        super(task, ddl);
        this.startDateTime = startDT;
    }

    public String getTaskDuration() {
        return this.startDateTime + "-" + super.getDeadline();
    }

    @Override
    public String toString() {
        return "[E]" + this.getStatusIcon() + " " + this.task + " (from: " + this.startDateTime + " to: " + this.deadline + ")";
    }

    @Override
    public String formatData() {
        String str = (super.getIsDone()) ? "1" : "0" + " | " + getTask();
        return "E | " + str + " | " + getTaskDuration();
    }
}
