import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class MegaBot {
    public static ArrayList<Task> taskList = new ArrayList<Task>();
    public static int arrCount = 0;


    public static void main(String[] args) throws InvalidTaskException {
        printDivider();
        String intro = "Hello! I'm MegaBot\n" +
                "What can I do for you?\n";
        System.out.println(intro);
        printDivider();
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            try {
                printDivider();

                Command cmd = Command.fromString(userInput);

                switch (cmd) {
                    case LIST:
                        printTasksArray();
                        break;
                    case TODO:
                        String todo = removeFirstWord(userInput);
                        saveToDoToArray(todo);
                        break;
                    case DEADLINE:
                        String deadline = removeFirstWord(userInput);
                        saveDeadlineToArray(deadline);
                        break;
                    case EVENT:
                        String event = removeFirstWord(userInput);
                        saveEventToArray(event);
                        break;
                    case UNMARK:
                        handleMarkCommand(userInput, false);
                        break;
                    case MARK:
                        handleMarkCommand(userInput, true);
                        break;
                    case DELETE:
                        handleDeleteCommand(userInput);
                        break;
                    case UNKNOWN:
                    default:
                        throw new InvalidTaskException("OOPSIE!! I can't create a task because I don't understand what task you're talking about :-(");
                }
            } catch (InvalidTaskException e){
                System.out.println(e.getMessage());
            } finally {
                printDivider();
                userInput = scanner.nextLine();
            }

        }

        printDivider();
        System.out.println("Bye. Hope to see you again soon!");
        printDivider();
        scanner.close();
        saveToFile();
    }

    // calls setTaskDone and setTaskUndone
    private static void handleMarkCommand(String userInput, boolean markAsDone) throws InvalidTaskException {
        String[] str = userInput.split(" ");
        if (str.length < 2) {
            String action = markAsDone ? "mark" : "unmark";
            throw new InvalidTaskException("OOPSIE!! Please specify which task to " + action + ".");
        }

        try {
            int taskNumber = Integer.parseInt(str[1]);
            if (taskNumber <= 0 || taskNumber > arrCount) {
                throw new InvalidTaskException("OOPSIE!! Task number " + taskNumber + " does not exist.");
            }

            if (markAsDone) {
                setTaskDone(taskNumber);
            } else {
                setTaskUndone(taskNumber);
            }
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("OOPSIE!! Please provide a valid task number.");
        }
    }

    public static void setTaskDone(Integer i) {
        taskList.get(i - 1).markAsDone();
        System.out.println("Nice! I've marked this task as done:\n");
        System.out.println(taskList.get(i - 1));
    }

    public static void setTaskUndone(Integer i) {
        taskList.get(i - 1).markAsUndone();
        System.out.println("OK, I've marked this task as not done yet:\n");
        System.out.println(taskList.get(i - 1));
    }

    private static void handleDeleteCommand(String userInput) throws InvalidTaskException {
        String[] str = userInput.split(" ");
        if (str.length < 2) {
            throw new InvalidTaskException("OOPSIE!! Please specify which task to delete.");
        }

        try {
            int taskNumber = Integer.parseInt(str[1]);
            if (taskNumber <= 0 || taskNumber > arrCount) {
                throw new InvalidTaskException("OOPSIE!! Task number " + taskNumber + " does not exist.");
            }
            deleteTask(taskNumber);
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("OOPSIE!! Please provide a valid task number.");
        }
    }

    public static void deleteTask(Integer i) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(taskList.get(i - 1));
        taskList.remove(i - 1);
        arrCount--;
        taskInArray();
    }

    public static void printTasksArray() {
        for (int i = 0; i < arrCount; i++) {
            System.out.println((i + 1) + "." + taskList.get(i));
        }
    }

    public static void saveToDoToArray(String task) throws InvalidTaskException{
        if (task.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of todo cannot be empty.");
        }
        System.out.println("Got it. I've added this task:");
        ToDo td = new ToDo(task);
        taskList.add(td);
        // tasksArray[arrCount] = td;
        arrCount++;
        System.out.println(td);
        taskInArray();
    }

    public static void saveDeadlineToArray(String task) throws InvalidTaskException{
        if (task.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of a deadline cannot be empty.");
        }

        String[] parts = task.split(" /(?:by) ");

        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! Please use format: deadline <task> /by <date>");
        }
        System.out.println("Got it. I've added this task:");
        Deadline ddl = new Deadline(parts[0], parts[1]);
        taskList.add(ddl);
        // tasksArray[arrCount] = ddl;
        arrCount++;
        System.out.println(ddl);
        taskInArray();
    }

    public static void saveEventToArray(String task) throws InvalidTaskException{
        if (task.trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! The description of an event cannot be empty.");
        }

        String[] parts = task.split(" /(?:from|to) ");
        if (parts.length != 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new InvalidTaskException("OOPSIE!! Please use format: event <task> /from <start> /to <end>");
        }

        System.out.println("Got it. I've added this task:");
        Event event = new Event(parts[0], parts[1], parts[2]);
        taskList.add(event);
        // tasksArray[arrCount] = event;
        arrCount++;
        System.out.println(event);
        taskInArray();
    }

    public static void taskInArray() {
        System.out.printf("Now you have %d tasks in the list%n", arrCount);
    }

    public static String removeFirstWord(String str) {
        int index = str.indexOf(' ');
        if (index == -1) {
            return "";
        }
        return str.substring(index + 1);
    }

    public static void printDivider() {
        System.out.println("____________________________________________________________");
    }

    public static void saveToFile() {
        /**
         * at the end of the program, pass to this function
         *
         * open file
         *      for each line in the array, print it out in the file
         */
        try {
            FileWriter writer = new FileWriter("data/duke.txt");
            for (int i = 0; i < arrCount; i++) {
                writer.write(taskList.get(i).formatData());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing into a file.");
            throw new RuntimeException(e);
        }

    }
}
