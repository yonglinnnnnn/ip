package megabot;

import megabot.task.Task;

import java.util.Scanner;

/**
 * Handles all user interface interactions for the MegaBot application.
 * Manages input reading and output display for various application states.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a Ui object and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the application starts.
     */

    public void showWelcome() {
        printDivider();
        String intro = "Hello! I'm MegaBot\n" +
                "What can I do for you?\n";
        System.out.println(intro);
        printDivider();
        System.out.println();
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showGoodbye() {
        printDivider();
        System.out.println("Bye. Hope to see you again soon!");
        printDivider();
    }

    /**
     * Reads a line of user input from the console.
     *
     * @return the user input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message when tasks cannot be loaded from file.
     */
    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with empty task list.");
    }

    /**
     * Displays a confirmation message when a task is successfully added.
     *
     * @param task the task that was added
     * @param totalTasks the total number of tasks after addition
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        showTaskCount(totalTasks);
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n");
        System.out.println(task);
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:\n");
        System.out.println(task);
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task the task that was deleted
     * @param totalTasks the total number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        showTaskCount(totalTasks);
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
     * Displays the search results for tasks matching a keyword.
     *
     * @param tasks the list of matching tasks to display
     * @param keyword the keyword that was searched for
     */
    public void showFoundTasks(java.util.ArrayList<Task> tasks, String keyword) {
        if (tasks.isEmpty()) {
            System.out.println("No matching tasks found for keyword: " + keyword);
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
    }

    /**
     * Displays the current number of tasks in the list.
     *
     * @param count the number of tasks
     */
    private void showTaskCount(int count) {
        System.out.printf("Now you have %d tasks in the list%n", count);
    }

    /**
     * Prints a visual divider line to separate sections of output.
     */
    public void printDivider() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Closes the scanner to release resources.
     */
    public void close() {
        scanner.close();
    }
}