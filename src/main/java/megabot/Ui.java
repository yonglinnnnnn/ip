package megabot;

import megabot.task.Task;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        printDivider();
        String intro = "Hello! I'm MegaBot\n" +
                "What can I do for you?\n";
        System.out.println(intro);
        printDivider();
        System.out.println();
    }

    public void showGoodbye() {
        printDivider();
        System.out.println("Bye. Hope to see you again soon!");
        printDivider();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with empty task list.");
    }

    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        showTaskCount(totalTasks);
    }

    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n");
        System.out.println(task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:\n");
        System.out.println(task);
    }

    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        showTaskCount(totalTasks);
    }

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

    private void showTaskCount(int count) {
        System.out.printf("Now you have %d tasks in the list%n", count);
    }

    public void printDivider() {
        System.out.println("____________________________________________________________");
    }

    public void close() {
        scanner.close();
    }
}