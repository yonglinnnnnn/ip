package megabot;

import java.io.IOException;
import java.util.ArrayList;

import megabot.exception.InvalidTaskException;
import megabot.task.Deadline;
import megabot.task.Event;
import megabot.task.Task;
import megabot.task.ToDo;

/**
 * Main class for the MegaBot task management application.
 * Manages the interaction between the user interface, task storage, and task operations.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class MegaBot {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;
    private GuiUi guiUi;

    /**
     * Constructs a MegaBot instance with the specified file path for task storage.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public MegaBot(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be null or empty";

        ui = new Ui();
        guiUi = new GuiUi();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.load());
        } catch (InvalidTaskException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Default constructor that uses the default file path.
     */
    public MegaBot() {
        this("data/duke.txt");
    }

    /**
     * Generates a response for the user's input (for GUI use).
     * This method processes user commands and returns appropriate responses.
     *
     * @param input the user input string
     * @return the response message from MegaBot
     */
    public String getResponse(String input) {
        assert input != null : "User input cannot be null";

        try {
            return handleCommandForGui(input);
        } catch (InvalidTaskException e) {
            return e.getMessage();
        } finally {
            // Save tasks after each command (for GUI)
            saveTasksToFile();
        }
    }

    /**
     * Handles commands and returns response strings (for GUI use).
     *
     * @param userInput the user input string
     * @return the response message
     * @throws InvalidTaskException if there's an error processing the command
     */
    private String handleCommandForGui(String userInput) throws InvalidTaskException {
        assert userInput != null : "User input cannot be null";

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
        assert userInput != null : "User input cannot be null";

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
     * Runs the main application loop.
     * Displays welcome message, processes user commands, and handles cleanup.
     */
    public void run() {
        ui.showWelcome();

        String userInput = ui.readCommand();

        while (!userInput.equals("bye")) {
            try {
                ui.printDivider();
                handleCommand(userInput);
            } catch (InvalidTaskException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.printDivider();
                userInput = ui.readCommand();
            }
        }

        ui.showGoodbye();
        ui.close();
        saveTasksToFile();
    }

    /**
     * Handles user command
     */
    public void handleCommand(String userInput) throws InvalidTaskException {
        Command cmd = Parser.parseCommand(userInput);

        switch (cmd) {
        case LIST:
            ui.showTaskList(tasks.getTasks());
            break;
        case TODO:
            handleTodoCommand(userInput);
            break;
        case DEADLINE:
            handleDeadlineCommand(userInput);
            break;
        case EVENT:
            handleEventCommand(userInput);
            break;
        case MARK:
            handleMarkCommand(userInput, true);
            break;
        case UNMARK:
            handleMarkCommand(userInput, false);
            break;
        case DELETE:
            handleDeleteCommand(userInput);
            break;
        case FIND:
            handleFindCommand(userInput);
            break;
        case UNKNOWN:
        default:
            throw new InvalidTaskException("OOPSIE!! I can't create a task because "
                + "I don't understand what task you're talking about :-(");
        }
    }

    /**
     * Handles the creation of a new todo task.
     *
     * @param userInput the user input containing the todo command and description
     * @throws InvalidTaskException if the todo description is empty
     */
    private void handleTodoCommand(String userInput) throws InvalidTaskException {
        String taskDescription = Parser.removeFirstWord(userInput);
        if (taskDescription.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of todo cannot be empty.");
        }

        ToDo todo = new ToDo(taskDescription);
        tasks.addTask(todo);
        ui.showTaskAdded(todo, tasks.size());
    }

    /**
     * Handles the creation of a new deadline task.
     *
     * @param userInput the user input containing the deadline command and parameters
     * @throws InvalidTaskException if the deadline format is invalid
     */
    private void handleDeadlineCommand(String userInput) throws InvalidTaskException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseDeadline(taskContent);

        Deadline deadline = new Deadline(parts[0], parts[1]);
        tasks.addTask(deadline);
        ui.showTaskAdded(deadline, tasks.size());
    }

    /**
     * Handles the creation of a new event task.
     *
     * @param userInput the user input containing the event command and parameters
     * @throws InvalidTaskException if the event format is invalid
     */
    private void handleEventCommand(String userInput) throws InvalidTaskException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseEvent(taskContent);

        Event event = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(event);
        ui.showTaskAdded(event, tasks.size());
    }

    /**
     * Handles marking or unmarking a task as done.
     *
     * @param userInput the user input containing the mark/unmark command and task number
     * @param markAsDone true to mark as done, false to mark as undone
     * @throws InvalidTaskException if the task number is invalid
     */

    private void handleMarkCommand(String userInput, boolean markAsDone) throws InvalidTaskException {
        int taskNumber = Parser.parseTaskNumber(userInput);
        int taskIndex = taskNumber - 1; // Convert to 0-based index

        if (!tasks.isValidIndex(taskIndex)) {
            throw new InvalidTaskException("OOPSIE!! Task number " + taskNumber + " does not exist.");
        }

        if (markAsDone) {
            tasks.markTask(taskIndex);
            ui.showTaskMarked(tasks.getTask(taskIndex));
        } else {
            tasks.unmarkTask(taskIndex);
            ui.showTaskUnmarked(tasks.getTask(taskIndex));
        }
    }

    /**
     * Handles the deletion of a task.
     *
     * @param userInput the user input containing the delete command and task number
     * @throws InvalidTaskException if the task number is invalid
     */
    private void handleDeleteCommand(String userInput) throws InvalidTaskException {
        int taskNumber = Parser.parseTaskNumber(userInput);
        int taskIndex = taskNumber - 1; // Convert to 0-based index

        if (!tasks.isValidIndex(taskIndex)) {
            throw new InvalidTaskException("OOPSIE!! Task number " + taskNumber + " does not exist.");
        }

        Task deletedTask = tasks.getTask(taskIndex);
        tasks.deleteTask(taskIndex);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Handles the search for tasks containing a keyword.
     *
     * @param userInput the user input containing the find command and keyword
     * @throws InvalidTaskException if no keyword is provided
     */
    private void handleFindCommand(String userInput) throws InvalidTaskException {
        String keyword = Parser.parseFindKeyword(userInput);
        ArrayList<Task> foundTasks = tasks.findTasks(keyword);
        ui.showFoundTasks(foundTasks, keyword);
    }


    /**
     * Saves all tasks to the storage file.
     */
    private void saveTasksToFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("An error occurred when writing to file: " + e.getMessage());
        }
    }

    /**
     * Main entry point of the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new MegaBot("data/duke.txt").run();
    }
}
