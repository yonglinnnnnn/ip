package megabot;

import megabot.task.Deadline;
import megabot.task.Event;
import megabot.task.Task;
import megabot.task.ToDo;

/**
 * GUI-adapted version of Ui that returns formatted strings instead of printing to console.
 * Reuses all the formatting logic from the original Ui class.
 */
public class GuiUi {

    /**
     * Returns the welcome message as a formatted string.
     * @return the welcome message
     */
    public String getWelcomeMessage() {
        return "Hello from MegaBot!\n" + "\nWhat can I do for you today?";
    }

    /**
     * Returns the goodbye message as a formatted string.
     * @return the goodbye message
     */
    public String getGoodbyeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns a formatted string when a task is successfully added.
     * @param task the task that was added
     * @param totalTasks the total number of tasks in the list after adding
     * @return formatted confirmation message
     */
    public String getTaskAddedMessage(Task task, int totalTasks) {
        return String.format("Got it. I've added this task:\n  %s\nNow you have %d tasks in the list.",
                task.toString(), totalTasks);
    }

    /**
     * Returns a formatted string when a task is marked as done.
     * @param task the task that was marked as done
     * @return formatted confirmation message
     */
    public String getTaskMarkedMessage(Task task) {
        return "Nice! I've marked this task as done:\n  " + task.toString();
    }

    /**
     * Returns a formatted string when a task is unmarked.
     * @param task the task that was unmarked
     * @return formatted confirmation message
     */
    public String getTaskUnmarkedMessage(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task.toString();
    }

    /**
     * Returns a formatted string showing the complete list of tasks.
     * @param tasks the TaskList containing all tasks to display
     * @return formatted task list
     */
    public String getTaskListMessage(TaskList tasks) {
        if (tasks.size() == 0) {
            return "Your task list is empty!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.getTask(i).toString()));
        }
        return sb.toString().trim();
    }

    /**
     * Returns a formatted string when a task is deleted.
     * @param task the task that was deleted
     * @param totalTasks the total number of tasks remaining in the list
     * @return formatted confirmation message
     */
    public String getTaskDeletedMessage(Task task, int totalTasks) {
        return String.format("Noted. I've removed this task:\n  %s\nNow you have %d tasks in the list.",
                task.toString(), totalTasks);
    }

    /**
     * Returns a formatted error message for invalid commands.
     * @return error message
     */
    public String getInvalidCommandMessage() {
        return "OOPS!!! I'm sorry, but I don't know what that means :-(";
    }

    /**
     * Returns a formatted error message.
     * @param message the error message to format
     * @return formatted error message
     */
    public String getErrorMessage(String message) {
        return "OOPS!!! " + message;
    }

    /**
     * Returns a formatted string for loading error.
     * @return loading error message
     */
    public String getLoadingErrorMessage() {
        return "Error loading tasks from file. Starting with empty task list.";
    }

    /**
     * Returns a formatted string showing matching tasks from find command.
     * @param tasks the TaskList containing matching tasks
     * @return formatted search results
     */
    public String getFindResultsMessage(TaskList tasks) {
        if (tasks.size() == 0) {
            return "No matching tasks found in your list.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.getTask(i).toString()));
        }
        return sb.toString().trim();
    }

    /**
     * Determines the display string for different task types.
     * @param task the task whose type string is needed
     * @return a string representing the task type for display purposes
     */
    private String getTaskTypeString(Task task) {
        if (task instanceof ToDo) {
            return "Todo";
        } else if (task instanceof Deadline) {
            return "Deadline";
        } else if (task instanceof Event) {
            return "Event";
        }
        return "Task";
    }
}
