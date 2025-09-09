package megabot.gui;

import java.io.IOException;
import java.util.ArrayList;

import megabot.Command;
import megabot.Parser;
import megabot.Storage;
import megabot.TaskList;
import megabot.Ui;
import megabot.exception.InvalidTaskException;
import megabot.task.Deadline;
import megabot.task.Event;
import megabot.task.Task;
import megabot.task.ToDo;

/**
 * Handles user input from GUI
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class GuiCommandHandler {
    private final TaskList tasks;
    private final Storage storage;

    /**
     * Constructor for GuiCommandHandler
     *
     * @param tasks the tasklist
     */
    public GuiCommandHandler(TaskList tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    /**
     * Handles commands and returns response strings (for GUI use).
     *
     * @param userInput the user input string
     * @return the response message
     * @throws InvalidTaskException if there's an error processing the command
     */
    public String handleCommandForGui(String userInput) throws InvalidTaskException {
        if (userInput.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }

        Command cmd = Parser.parseCommand(userInput);

        switch (cmd) {
        case LIST:
            return getTaskListString();
        case TODO:
            return handleTodoCommandForGui(userInput);
        case DEADLINE:
            return handleDeadlineCommandForGui(userInput);
        case EVENT:
            return handleEventCommandForGui(userInput);
        case MARK:
            return handleMarkCommandForGui(userInput, true);
        case UNMARK:
            return handleMarkCommandForGui(userInput, false);
        case DELETE:
            return handleDeleteCommandForGui(userInput);
        case FIND:
            return handleFindCommandForGui(userInput);
        case UNKNOWN:
        default:
            throw new InvalidTaskException("OOPSIE!! I can't create a task because "
                    + "I don't understand what task you're talking about :-(");
        }
    }

    /**
     * Returns a formatted string of all tasks.
     *
     * @return formatted task list string
     */
    private String getTaskListString() {
        if (tasks.isEmpty()) {
            return "You have no tasks in your list.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        ArrayList<Task> taskList = tasks.getTasks();
        for (int i = 0; i < taskList.size(); i++) {
            sb.append((i + 1)).append(".").append(taskList.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Handles the creation of a new todo task for GUI.
     *
     * @param userInput the user input containing the todo command and description
     * @return response message
     * @throws InvalidTaskException if the todo description is empty
     */
    private String handleTodoCommandForGui(String userInput) throws InvalidTaskException {
        String taskDescription = Parser.removeFirstWord(userInput);
        if (taskDescription.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of todo cannot be empty.");
        }

        ToDo todo = new ToDo(taskDescription);
        tasks.addTask(todo);
        return "Got it. I've added this task:\n" + todo + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles the creation of a new deadline task for GUI.
     *
     * @param userInput the user input containing the deadline command and parameters
     * @return response message
     * @throws InvalidTaskException if the deadline format is invalid
     */
    private String handleDeadlineCommandForGui(String userInput) throws InvalidTaskException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseDeadline(taskContent);

        Deadline deadline = new Deadline(parts[0], parts[1]);
        tasks.addTask(deadline);
        return "Got it. I've added this task:\n" + deadline + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles the creation of a new event task for GUI.
     *
     * @param userInput the user input containing the event command and parameters
     * @return response message
     * @throws InvalidTaskException if the event format is invalid
     */
    private String handleEventCommandForGui(String userInput) throws InvalidTaskException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseEvent(taskContent);

        Event event = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(event);
        return "Got it. I've added this task:\n" + event + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles marking or unmarking a task as done for GUI.
     *
     * @param userInput the user input containing the mark/unmark command and task number
     * @param markAsDone true to mark as done, false to mark as undone
     * @return response message
     * @throws InvalidTaskException if the task number is invalid
     */
    private String handleMarkCommandForGui(String userInput, boolean markAsDone) throws InvalidTaskException {
        int taskNumber = Parser.parseTaskNumber(userInput);
        int taskIndex = taskNumber - 1; // Convert to 0-based index

        if (!tasks.isValidIndex(taskIndex)) {
            throw new InvalidTaskException("OOPSIE!! Task number " + taskNumber + " does not exist.");
        }

        if (markAsDone) {
            tasks.markTask(taskIndex);
            return "Nice! I've marked this task as done:\n" + tasks.getTask(taskIndex);
        } else {
            tasks.unmarkTask(taskIndex);
            return "OK, I've marked this task as not done yet:\n" + tasks.getTask(taskIndex);
        }
    }

    /**
     * Handles the deletion of a task for GUI.
     *
     * @param userInput the user input containing the delete command and task number
     * @return response message
     * @throws InvalidTaskException if the task number is invalid
     */
    private String handleDeleteCommandForGui(String userInput) throws InvalidTaskException {
        int taskNumber = Parser.parseTaskNumber(userInput);
        int taskIndex = taskNumber - 1; // Convert to 0-based index

        if (!tasks.isValidIndex(taskIndex)) {
            throw new InvalidTaskException("OOPSIE!! Task number " + taskNumber + " does not exist.");
        }

        Task deletedTask = tasks.getTask(taskIndex);
        tasks.deleteTask(taskIndex);
        return "Noted. I've removed this task:\n" + deletedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles the search for tasks containing a keyword for GUI.
     *
     * @param userInput the user input containing the find command and keyword
     * @return response message with found tasks
     * @throws InvalidTaskException if no keyword is provided
     */
    private String handleFindCommandForGui(String userInput) throws InvalidTaskException {
        String keyword = Parser.parseFindKeyword(userInput);
        ArrayList<Task> foundTasks = tasks.findTasks(keyword);

        if (foundTasks.isEmpty()) {
            return "No matching tasks found for keyword: " + keyword;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the matching tasks in your list:\n");
            for (int i = 0; i < foundTasks.size(); i++) {
                sb.append((i + 1)).append(".").append(foundTasks.get(i)).append("\n");
            }
            return sb.toString().trim();
        }
    }

    /**
     * Saves all tasks to the storage file.
     */
    private void saveTasksToFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            Ui ui = new Ui();
            ui.showError("An error occurred when writing to file: " + e.getMessage());
        }
    }
}
