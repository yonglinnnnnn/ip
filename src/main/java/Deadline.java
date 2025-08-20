public class Deadline extends Task{
    String deadline;

    public Deadline(String task, String ddl) {
        super(task);
        this.deadline = ddl;
    }

    @Override
    public String toString() {
        return "[D]" + this.getStatusIcon() + " " + this.task + " (by: " + this.deadline + ")";
    }
}
