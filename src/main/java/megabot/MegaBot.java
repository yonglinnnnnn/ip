package megabot;

import megabot.exception.InvalidTaskException;
import megabot.task.*;
import java.io.IOException;

public class MegaBot {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public MegaBot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (InvalidTaskException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

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

    private void handleCommand(String userInput) throws InvalidTaskException {
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
        case UNKNOWN:
        default:
            throw new InvalidTaskException("OOPSIE!! I can't create a task because I don't understand what task you're talking about :-(");
        }
    }

    private void handleTodoCommand(String userInput) throws InvalidTaskException {
        String taskDescription = Parser.removeFirstWord(userInput);
        if (taskDescription.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of todo cannot be empty.");
        }

        ToDo todo = new ToDo(taskDescription);
        tasks.addTask(todo);
        ui.showTaskAdded(todo, tasks.size());
    }

    private void handleDeadlineCommand(String userInput) throws InvalidTaskException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseDeadline(taskContent);

        Deadline deadline = new Deadline(parts[0], parts[1]);
        tasks.addTask(deadline);
        ui.showTaskAdded(deadline, tasks.size());
    }

    private void handleEventCommand(String userInput) throws InvalidTaskException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseEvent(taskContent);

        Event event = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(event);
        ui.showTaskAdded(event, tasks.size());
    }

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

    private void saveTasksToFile() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("An error occurred when writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MegaBot("data/duke.txt").run();
    }
}