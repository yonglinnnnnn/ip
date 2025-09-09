package megabot.task;

/**
 * Represents a generic task with a description and completion status.
 * This is the base class for all types of tasks in the MegaBot application.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Task {
    private final String task;
    private Boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param task the description of the task
     */
    public Task(String task) {
        assert task != null : "Task description cannot be null";
        this.task = task;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        assert !this.isDone : "Task is already marked as done";
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void markAsUndone() {
        assert this.isDone : "Task should be done before unmarking";
        this.isDone = false;
    }

    /**
     * Returns the status icon representation of the task.
     *
     * @return "[X]" if task is done, "[ ]" if not done
     */
    public String getStatusIcon() {
        return (this.isDone ? "[X]" : "[ ]");
    }

    /**
     * Returns the completion status of the task.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getTask() {
        return this.task;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + this.getTask();
    }

    /**
     * Returns the task data in a format suitable for file storage.
     * Format: "STATUS | DESCRIPTION" where STATUS is "1" for done, "0" for not done.
     *
     * @return formatted string for file storage
     */
    public String formatData() {
        String status = this.getIsDone() ? "1" : "0";
        return status + " | " + this.getTask();
    }
}
