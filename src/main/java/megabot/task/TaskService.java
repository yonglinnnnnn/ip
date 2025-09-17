package megabot.task;

import java.util.ArrayList;
import java.util.logging.Logger;

import megabot.exception.MegabotException;
import megabot.parser.Parser;

/**
 * Task Service class to handle the creation of task(s)
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class TaskService {
    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());
    private final TaskList tasks;

    /**
     * Constructor to initialise the List of tasks to perform the Create, View, and Delete of tasks
     * @param tasks TaskList which contains all tasks
     */
    public TaskService(TaskList tasks) {
        this.tasks = tasks;
        LOGGER.info("TaskService initialized with " + tasks.size() + " existing tasks");
    }

    /**
     * Creates a new Todo task and adds it into the tasklist
     *
     * @param userInput     User input
     * @return Todo
     * @throws MegabotException if the task number is invalid
     */
    public ToDo createTodoTask(String userInput) throws MegabotException {
        String taskDescription = Parser.removeFirstWord(userInput);

        if (taskDescription.trim().isEmpty()) {
            throw new MegabotException("OOPSIE!! The description of todo cannot be empty.");
        }

        ToDo todo = new ToDo(taskDescription);
        tasks.addTask(todo);

        return todo;
    }

    /**
     * Creates a new Todo task and adds it into the tasklist
     *
     * @param userInput     User input
     * @return Todo
     * @throws MegabotException if the task number is invalid
     */
    public Deadline createDeadlineTask(String userInput) throws MegabotException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseDeadline(taskContent);

        Deadline deadline = new Deadline(parts[0], parts[1]);
        tasks.addTask(deadline);

        return deadline;
    }

    /**
     * Creates a new Todo task and adds it into the tasklist
     *
     * @param userInput     User input
     * @return Todo
     * @throws MegabotException if the task number is invalid
     */
    public Event createEventTask(String userInput) throws MegabotException {
        String taskContent = Parser.removeFirstWord(userInput);
        String[] parts = Parser.parseEvent(taskContent);

        Event event = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(event);

        return event;
    }

    /**
     * Marks task as done
     * @param taskIndex index of task in taskList
     */
    public void markTask(int taskIndex) {
        tasks.markTask(taskIndex);
    }

    /**
     * Marks task as undone
     * @param taskIndex index of task in taskList
     */
    public void unmarkTask(int taskIndex) {
        tasks.unmarkTask(taskIndex);
    }

    /**
     * Deletes task
     * @param taskIndex index of task in taskList
     * @throws MegabotException if the task number is invalid
     */
    public Task deleteTask(int taskIndex) throws MegabotException {
        Task deletedTask = tasks.getTask(taskIndex);
        tasks.deleteTask(taskIndex);

        return deletedTask;
    }

    /**
     * Find tasks with matching keyword
     * @param userInput     Word to find in TaskList
     * @return              String that contains all the tasks that contains the keyword
     */
    public String findTask(String userInput) throws MegabotException {
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
}
