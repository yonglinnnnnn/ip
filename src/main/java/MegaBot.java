import java.util.Scanner;

public class MegaBot {
    public static void main(String[] args) {
        String intro = "____________________________________________________________\n" +
                "Hello! I'm MegaBot\n" +
                "What can I do for you?\n" +
                "____________________________________________________________\n\n";
        System.out.println(intro);
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while (!userInput.equals("bye")) {
            userInput = scanner.nextLine(); // Reads a String
            System.out.print("     ____________________________________________________________\n");

            System.out.println("     " + userInput);
            System.out.print("     ____________________________________________________________\n");
        }

        scanner.close();

    }
}
