import java.util.ArrayList;
import java.util.Scanner;

public class MegaBot {
   public static Task[] tasksArray = new Task[100];
   public static int arrCount = 0;


    public static void main(String[] args) {
        String intro = "____________________________________________________________\n" +
                "Hello! I'm MegaBot\n" +
                "What can I do for you?\n" +
                "____________________________________________________________\n\n";
        System.out.println(intro);
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine(); // Reads a String;

        while (!userInput.equals("bye")) {
            System.out.print("     ____________________________________________________________\n");

            if (userInput.equals("list")) {
                // for each item in the arr, print it out
                printTasksArray();
            } else if (userInput.contains("unmark")) {
                // call markAsUndone
                String[] str = userInput.split(" ");
                Integer i = Integer.parseInt(str[1]);
                setTaskUndone(i);
            } else if (userInput.contains("mark")) {
                // call markAsDone
                String[] str = userInput.split(" ");
                Integer i = Integer.parseInt(str[1]);
                setTaskDone(i);
            } else {
                // implement save to arr
                saveTasksToArray(userInput);
            }

            System.out.println("     ____________________________________________________________\n");
            userInput = scanner.nextLine();
        }

        System.out.println("    ____________________________________________________________\n" +
                "    Bye. Hope to see you again soon!\n" +
                "    ____________________________________________________________");
        scanner.close();
    }

    public static void setTaskDone(Integer i) {
        tasksArray[i - 1].markAsDone();
        System.out.println("     Nice! I've marked this task as done:\n");
        System.out.println("     " + tasksArray[i - 1]);
    }

    public static void setTaskUndone(Integer i) {
        tasksArray[i - 1].markAsUndone();
        System.out.println("     OK, I've marked this task as not done yet:\n");
        System.out.println("     " + tasksArray[i - 1]);
    }

    public static void printTasksArray() {
        for (int i = 0; i < arrCount; i++) {
            System.out.println("     " + (i+1) + "." + tasksArray[i]);
        }
    }

    public static void saveTasksToArray(String task) {
        Task t = new Task(task);
        tasksArray[arrCount] = t;
        arrCount++;
        System.out.println("     added: " + t);
    }
}
