package megabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import megabot.exception.InvalidTaskException;
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
     * @throws InvalidTaskException if there is an error parsing tasks from the file
     */
    public ArrayList<Task> load() throws InvalidTaskException {
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
     * @throws InvalidTaskException if there is an error creating the task
     */
    private Task parseTaskFromFile(String line) throws InvalidTaskException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null; // Skip invalid lines
        }

        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");
        String taskDescription = parts[2];

        Task task = null;

        switch (taskType) {
        case "T":
            task = new ToDo(taskDescription);
            break;
        case "D":
            if (parts.length >= 4) {
                task = new Deadline(taskDescription, parts[3]);
            }
            break;
        case "E":
            if (parts.length >= 4) {
                // Parse the duration string (start-end format)
                String[] dateParts = parts[3].split("-");
                if (dateParts.length >= 2) {
                    task = new Event(taskDescription, dateParts[0], dateParts[1]);
                }
            }
            break;
        default:
            break;
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }
}
