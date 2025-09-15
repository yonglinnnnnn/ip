package megabot;

import java.io.IOException;
import java.util.ArrayList;

import megabot.exception.InvalidTaskException;
import megabot.gui.Gui;
import megabot.task.Task;
import megabot.task.TaskList;

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
    private Gui gui;

    /**
     * Constructs a MegaBot instance with the specified file path for task storage.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public MegaBot(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be null or empty";

        ui = new Ui();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.load());
            gui = new Gui(tasks, storage);
        } catch (InvalidTaskException e) {
            gui.showLoadingError();
            tasks = new TaskList();
        }
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
            return gui.handleCommand(input);
        } catch (InvalidTaskException e) {
            return e.getMessage();
        } finally {
            // Save tasks after each command (for GUI)
            saveTasksToFile();
        }
    }

    /**
     * Method to ask for user input
     */
    public void processUserCommands() {
        String userInput = ui.readCommand();

        while (!userInput.equals("bye")) {
            try {
                ui.printDivider();
                processCommands(userInput);
            } catch (InvalidTaskException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.printDivider();
                userInput = ui.readCommand();
            }
        }
    }

    /**
     * Handles user command
     */
    public void processCommands(String userInput) throws InvalidTaskException {
        Command cmd = Parser.parseCommand(userInput);

        switch (cmd) {
        case LIST:
            ui.showTaskList(tasks.getTasks());
            break;
        case TODO:
            gui.handleTodoCommand(userInput);
            break;
        case DEADLINE:
            gui.handleDeadlineCommand(userInput);
            break;
        case EVENT:
            gui.handleEventCommand(userInput);
            break;
        case MARK:
            gui.handleMarkCommand(userInput, true);
            break;
        case UNMARK:
            gui.handleMarkCommand(userInput, false);
            break;
        case DELETE:
            gui.handleDeleteCommand(userInput);
            break;
        case FIND:
            gui.handleFindCommand(userInput);
            break;
        case UNKNOWN:
            throw new InvalidTaskException("OOPSIE!! Unknown command type found");
        default:
            throw new InvalidTaskException("OOPSIE!! I can't create a task because "
                + "I don't understand what task you're talking about :-(");
        }
    }


    private int getTaskNumber(String userInput) throws InvalidTaskException {
        return Parser.parseTaskNumber(userInput);
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
}
