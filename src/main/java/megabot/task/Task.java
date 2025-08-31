package megabot.task;

public class Task {
    private final String task;
    private Boolean isDone;

    public Task(String task) {
        this.task = task;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (this.isDone ? "[X]" : "[ ]");
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public String getTask() {
        return this.task;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + this.getTask();
    }

    /**
     * @return String that contains the task megabot.data for file writing
     */
    public String formatData() {
        String status = this.getIsDone() ? "1" : "0";
        return status + " | " + this.getTask();
    }
}
