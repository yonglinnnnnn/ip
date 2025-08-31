package megabot.task;

/**
 * Represents a simple todo task without any time constraints.
 * A todo task only has a description and completion status.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class ToDo extends Task {
    /**
     * Constructs a ToDo task with the specified description.
     *
     * @param todo the description of the todo task
     */
    public ToDo(String todo) {
        super(todo);
    }

    @Override
    public String toString() {
        return "[T]" + this.getStatusIcon() + " " + super.getTask();
    }

    /**
     * Returns the todo task data in a format suitable for file storage.
     * Format: "T | STATUS | DESCRIPTION"
     *
     * @return formatted string for file storage
     */
    @Override
    public String formatData() {
        return "T | " + super.formatData();
    }
}
