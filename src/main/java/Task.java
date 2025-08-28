public class Task {
    String task;
    Boolean isDone;

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
        return getStatusIcon() + " " + getTask();
    }

    /**
     * @return String that contains the task data for file writing
     */
    public String formatData() {
        return (this.isDone) ? "1" : "0" + " | " + getTask();
    }
}
