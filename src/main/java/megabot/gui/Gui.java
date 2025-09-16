package megabot.gui;

import java.io.IOException;
import java.util.ArrayList;

import megabot.Command;
import megabot.Parser;
import megabot.Storage;
import megabot.exception.MegabotException;
import megabot.task.Deadline;
import megabot.task.Event;
import megabot.task.Task;
import megabot.task.TaskList;
import megabot.task.TaskService;
import megabot.task.ToDo;

/**
 * Handles user input from GUI
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Gui {
    private final TaskList tasks;
    private final Storage storage;
    private final TaskService taskService;

    /**
     * Constructor for Gui
     *
     * @param tasks the tasklist
     */
    public Gui(TaskList tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
        this.taskService = new TaskService(tasks);
    }

    /**
     * Displays the welcome message when the application starts.
     */

    public static String showWelcome() {
        return "Hello! I'm MegaBot, your favourite yellow dinosaur bot!\n" + "What brings you here today?\n"
                + "By the way, just in case you didn't know, "
                + "I have to remind you that you have a lot of work in your backlog!!\n";
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public static String showGoodbye() {
        return "Sigh... You're neglecting your backlog again.\n"
                + "Come back soon or else you won't finish them!!!";
    }

    /**
     * Handles commands and returns response strings (for GUI use).
     *
     * @param userInput the user input string
     * @return the response message
     * @throws MegabotException if there's an error processing the command
     */
    public String handleCommand(String userInput) throws MegabotException {
        if (userInput.equals("bye")) {
            return showGoodbye();
        }

        Command cmd = Parser.parseCommand(userInput);

        switch (cmd) {
        case LIST:
            return getTaskListString();
        case TODO:
            return handleTodoCommand(userInput);
        case DEADLINE:
            return handleDeadlineCommand(userInput);
        case EVENT:
            return handleEventCommand(userInput);
        case MARK:
            return handleMarkCommand(userInput, true);
        case UNMARK:
            return handleMarkCommand(userInput, false);
        case DELETE:
            return handleDeleteCommand(userInput);
        case FIND:
            return handleFindCommand(userInput);
        case UNKNOWN:
            throw new MegabotException("OOPSIE!! Unknown command type found");
        default:
            throw new MegabotException("OOPSIE!! I can't create a task because "
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
            return "Congratulations!! You have no tasks in your list.";
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
     * @throws MegabotException if the todo description is empty
     */
    public String handleTodoCommand(String userInput) throws MegabotException {
        ToDo todo = taskService.createTodoTask(userInput);

        return "Alright... One more task for you in your horrible backlog.\n"
                + "I've added this task: " + todo + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles the creation of a new deadline task for GUI.
     *
     * @param userInput the user input containing the deadline command and parameters
     * @return response message
     * @throws MegabotException if the deadline format is invalid
     */
    public String handleDeadlineCommand(String userInput) throws MegabotException {
        Deadline deadline = taskService.createDeadlineTask(userInput);

        return "Alright... One more task for you in your horrible backlog.\n"
                + "I've added this task: " + deadline + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles the creation of a new event task for GUI.
     *
     * @param userInput the user input containing the event command and parameters
     * @return response message
     * @throws MegabotException if the event format is invalid
     */
    public String handleEventCommand(String userInput) throws MegabotException {
        Event event = taskService.createEventTask(userInput);

        return "Alright... One more task for you in your horrible backlog.\n"
                + "I've added this task: " + event + "\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    private int getTaskIndex(String userInput) throws MegabotException {
        int taskNumber = Parser.parseTaskNumber(userInput);
        int taskIndex = taskNumber - 1; // Convert to 0-based index

        if (!tasks.isValidIndex(taskIndex)) {
            throw new MegabotException("OOPSIE!! Task number " + taskNumber + " does not exist.");
        }

        return taskIndex;
    }

    /**
     * Handles marking or unmarking a task as done for GUI.
     *
     * @param userInput the user input containing the mark/unmark command and task number
     * @param markAsDone true to mark as done, false to mark as undone
     * @return response message
     * @throws MegabotException if the task number is invalid
     */
    public String handleMarkCommand(String userInput, boolean markAsDone) throws MegabotException {
        int taskIndex = getTaskIndex(userInput);

        if (markAsDone) {
            taskService.markTask(taskIndex);
            return "HORRAY!!! You've completed a task!\n"
                    + "I've marked this task as done:\n" + tasks.getTask(taskIndex);
        } else {
            taskService.unmarkTask(taskIndex);
            return "You marked an uncompleted task?? "
                    + "OK FINE, I've marked this task as not done yet:\n" + tasks.getTask(taskIndex);
        }
    }

    /**
     * Handles the deletion of a task for GUI.
     *
     * @param userInput the user input containing the delete command and task number
     * @return response message
     * @throws MegabotException if the task number is invalid
     */
    public String handleDeleteCommand(String userInput) throws MegabotException {
        int taskIndex = getTaskIndex(userInput);

        Task deletedTask = taskService.deleteTask(taskIndex);

        return "Great job, I've removed this task:\n" + deletedTask + "\n"
                + "from you horrendously long backlog.\n"
                + "Now you have " + tasks.size() + " tasks in the list";
    }

    /**
     * Handles the search for tasks containing a keyword for GUI.
     *
     * @param userInput the user input containing the find command and keyword
     * @return response message with found tasks
     * @throws MegabotException if no keyword is provided
     */
    public String handleFindCommand(String userInput) throws MegabotException {
        return taskService.findTask(userInput);
    }

    /**
     * Saves all tasks to the storage file.
     */
    private void saveTasksToFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            showError("An error occurred when writing to file: " + e.getMessage());
        }
    }

    /**
     * Displays an error message when tasks cannot be loaded from file.
     */
    public String showLoadingError() {
        return "Error loading tasks from file. Starting with empty task list.";
    }

    /**
     * Displays all tasks in the task list with their indices.
     *
     * @param tasks the list of tasks to display
     */
    public void showTaskList(java.util.ArrayList<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }
}
