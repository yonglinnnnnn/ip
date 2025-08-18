import java.util.Scanner;

public class MegaBot {
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

            System.out.println("     " + userInput);
            System.out.println("     ____________________________________________________________\n");
            userInput = scanner.nextLine();
        }

        System.out.println("    ____________________________________________________________\n" +
                "    Bye. Hope to see you again soon!\n" +
                "    ____________________________________________________________");
        scanner.close();

    }
}
