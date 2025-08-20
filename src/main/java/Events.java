public class Events extends Deadlines{
    String startDateTime;

    public Events(String task, String ddl, String startDT) {
        super(task, ddl);
        this.startDateTime = startDT;
    }

    @Override
    public String toString() {
        return "[E]" + this.getStatusIcon() + " " + this.task + "(from: " + this.startDateTime + "to: " + this.deadline + ")";
    }
}
