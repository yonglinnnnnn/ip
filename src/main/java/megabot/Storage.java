package megabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import megabot.exception.MegabotException;
import megabot.task.Deadline;
import megabot.task.Event;
import megabot.task.Task;
import megabot.task.ToDo;


/**
 * Handles the loading and saving of tasks to and from a file.
 * Manages file I/O operations and task serialization/deserialization.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class Storage {
    private static final int MIN_TASK_PARTS = 3;
    private static final int TASK_PARTS_WITH_DATE = 4;
    private static final String DONE_STATUS = "1";
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int DATE_INDEX = 3;

    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     * If the file does not exist, returns an empty list.
     *
     * @return an ArrayList of tasks loaded from the file
     * @throws MegabotException if there is an error parsing tasks from the file
     */
    public ArrayList<Task> load() throws MegabotException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = parseTaskFromFile(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            // If file doesn't exist, return empty list
            // The file will be created when we save tasks
            System.out.println("File not found!! Please try again");
        } catch (MegabotException e2) {
            System.out.println("Invalid task found. Please check file");
        }

        return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     * Creates the directory structure if it doesn't exist.
     *
     * @param tasks the list of tasks to save
     * @throws IOException if there is an error writing to the file
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        // Create directory if it doesn't exist
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        FileWriter writer = new FileWriter(filePath);
        for (Task task : tasks) {
            writer.write(task.formatData());
            writer.write("\n");
        }
        writer.close();
    }

    /**
     * Parses a task from a line of text in the file format.
     * Expected format: "TYPE | STATUS | DESCRIPTION [| ADDITIONAL_INFO]"
     *
     * @param line the line of text to parse
     * @return the parsed Task object, or null if the line is invalid
     * @throws MegabotException if there is an error creating the task
     */
    private Task parseTaskFromFile(String line) throws MegabotException {
        String[] parts = line.split(" \\| ");
        if (parts.length == 0) {

        }

        if (parts.length < MIN_TASK_PARTS) {
            throw new MegabotException("There is invalid task found from the file!");
        }

        String taskType = parts[0];
        boolean isDone = parts[STATUS_INDEX].equals(DONE_STATUS);
        String taskDescription = parts[DESCRIPTION_INDEX];

        Task task = createTaskByType(taskType, taskDescription, parts);

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    private Task createTaskByType(String taskType, String description, String[] parts) throws MegabotException {
        switch (taskType) {
        case "T":
            return new ToDo(description);
        case "D":
            if (parts.length >= TASK_PARTS_WITH_DATE) {
                return new Deadline(description, parts[3]);
            }
            break;
        case "E":
            if (parts.length >= TASK_PARTS_WITH_DATE) {
                // parse the duration string (start-end format)
                String[] dateParts = parts[DATE_INDEX].split("-");
                if (dateParts.length >= 2) {
                    return new Event(description, dateParts[0], dateParts[STATUS_INDEX]);
                }
            }
            break;
        default:
            throw new MegabotException("An invalid task was found in the file!");
        }
        return null;
    }
}
