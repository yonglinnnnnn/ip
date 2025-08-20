import java.util.ArrayList;
import java.util.Scanner;

public class MegaBot {
   public static String[] tasksArray = new String[100];
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

    public static void printTasksArray() {
        for (int i = 0; i < arrCount; i++) {
            System.out.println("     " + (i+1) + "." + tasksArray[i]);
        }
    }

    public static void saveTasksToArray(String task) {
        tasksArray[arrCount] = task;
        arrCount++;
        System.out.println("     added: " + task);
    }
}
