public class Deadline extends Task{
    String deadline;

    public Deadline(String task, String ddl) {
        super(task);
        this.deadline = ddl;
    }

    public String getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D]" + this.getStatusIcon() + " " + this.task + " (by: " + this.deadline + ")";
    }

    @Override
    public String formatData() {
        return "D | " + super.formatData() + " | " + getDeadline();
    }
}
