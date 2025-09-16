package megabot;

import java.io.IOException;

import megabot.exception.MegabotException;
import megabot.gui.Gui;
import megabot.task.TaskList;

/**
 * megabot.Main class for the MegaBot task management application.
 * Manages the interaction between the user interface, task storage, and task operations.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class MegaBot {
    private final Storage storage;
    private TaskList tasks;
    private Gui gui;

    /**
     * Constructs a MegaBot instance with the specified file path for task storage.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public MegaBot(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be null or empty";

        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.load());
            gui = new Gui(tasks, storage);
        } catch (MegabotException e) {
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
        } catch (MegabotException e) {
            return e.getMessage();
        } finally {
            // Save tasks after each command (for GUI)
            saveTasksToFile();
        }
    }

    /**
     * Saves all tasks to the storage file.
     */
    private void saveTasksToFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            gui.showError("An error occurred when writing to file: " + e.getMessage());
        }
    }
}
