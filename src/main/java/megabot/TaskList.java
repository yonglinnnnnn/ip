package megabot;

import megabot.exception.InvalidTaskException;
import megabot.task.Task;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {

        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {

        this.tasks = tasks;
    }

    public void addTask(Task task) {

        tasks.add(task);
    }

    public void deleteTask(int index) throws InvalidTaskException {
        if (index < 0 || index > tasks.size()) {
            throw new InvalidTaskException("Please give a valid number to delete the task from!!");
        }
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    public void markTask(int index) {
        Task task = getTask(index);
        if (task != null) {
            task.markAsDone();
        }
    }

    public void unmarkTask(int index) {
        Task task = getTask(index);
        if (task != null) {
            task.markAsUndone();
        }
    }

    public int size() {

        return tasks.size();
    }

    public boolean isEmpty() {

        return tasks.isEmpty();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

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