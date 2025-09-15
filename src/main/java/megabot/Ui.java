package megabot;

import java.util.Scanner;

import megabot.task.Task;

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
