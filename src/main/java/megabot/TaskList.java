package megabot;

import megabot.exception.InvalidTaskException;
import megabot.task.Task;

import java.util.ArrayList;

/**
 * Represents a collection of tasks with operations to manage them.
 * Provides methods to add, delete, mark, and retrieve tasks.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the given list of tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {

        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {

        tasks.add(task);
    }

    /**
     * Deletes a task at the specified index.
     *
     * @param index the index of the task to delete (0-based)
     * @throws InvalidTaskException if the index is out of bounds
     */
    public void deleteTask(int index) throws InvalidTaskException {
        if (index < 0 || index > tasks.size()) {
            throw new InvalidTaskException("Please give a valid number to delete the task from!!");
        }
        if (index < tasks.size()) {
            tasks.remove(index);
        }
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index the index of the task to retrieve (0-based)
     * @return the task at the specified index, or null if index is invalid
     */
    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index the index of the task to mark (0-based)
     */
    public void markTask(int index) {
        Task task = getTask(index);
        if (task != null) {
            task.markAsDone();
        }
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index the index of the task to unmark (0-based)
     */
    public void unmarkTask(int index) {
        Task task = getTask(index);
        if (task != null) {
            task.markAsUndone();
        }
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {

        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the task list is empty, false otherwise
     */
    public boolean isEmpty() {

        return tasks.isEmpty();
    }

    /**
     * Returns the underlying ArrayList of tasks.
     *
     * @return the list of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Checks if the given index is valid for the current task list.
     *
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Finds all tasks that contain the specified keyword in their description.
     * The search is case-insensitive.
     *
     * @param keyword the keyword to search for
     * @return an ArrayList of tasks that contain the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getTask().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}