public class Deadlines extends Task{
    String deadline;

    public Deadlines(String task, String ddl) {
        super(task);
        this.deadline = ddl;
    }

    @Override
    public String toString() {
        return "[D]" + this.getStatusIcon() + " " + this.task + "(by: " + this.deadline + ")";
    }
}
