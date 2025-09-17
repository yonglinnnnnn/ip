package megabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(Storage.class.getName());
    private static final int TYPE_INDEX = 0;
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
        /*
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
         */
        ArrayList<Task> tasks = new ArrayList<>();
        int lineNumber = 0;
        int invalidTaskCount = 0;

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                LOGGER.info("File does not exist, starting with empty task list: " + filePath);
                return tasks;
            }

            if (!file.canRead()) {
                throw new MegabotException("OOPSIE!! Cannot read file: " + filePath
                        + ". Please check file permissions.");
            }

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        tasks.add(task);
                        LOGGER.fine("Successfully loaded task from line " + lineNumber);
                    }
                } catch (MegabotException e) {
                    invalidTaskCount++;
                    LOGGER.warning("Invalid task on line " + lineNumber + ": " + e.getMessage());

                    // Continue loading other tasks instead of failing completely
                    if (invalidTaskCount > 10) {
                        scanner.close();
                        throw new MegabotException("OOPSIE!! Too many invalid tasks in file. "
                                + "Please check your data file format.");
                    }
                }
            }
            scanner.close();

            if (invalidTaskCount > 0) {
                LOGGER.warning("Loaded " + tasks.size() + " valid tasks, skipped "
                        + invalidTaskCount + " invalid tasks.");
            }
        } catch (FileNotFoundException e) {
            LOGGER.info("File not found, starting with empty task list: " + filePath);
        } catch (Exception e) {
            throw new MegabotException("OOPSIE!! Unexpected error loading tasks: " + e.getMessage());
        }

        LOGGER.info("Successfully loaded " + tasks.size() + " tasks from file");
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
        if (line == null || line.trim().isEmpty()) {
            throw new MegabotException("Empty line found in file");
        }

        String[] parts = line.split(" \\| ");

        if (parts.length < MIN_TASK_PARTS) {
            throw new MegabotException("Invalid task format - insufficient parts. Expected at least "
                    + MIN_TASK_PARTS + " parts, found " + parts.length);
        }

        String taskType = parts[TYPE_INDEX].trim();
        String statusStr = parts[STATUS_INDEX].trim();
        String taskDescription = parts[DESCRIPTION_INDEX];

        // Validate task type
        if (!isValidTaskType(taskType)) {
            throw new MegabotException("Invalid task type: " + taskType
                    + ". Expected T, D, or E");
        }

        // Validate status
        if (!statusStr.equals("0") && !statusStr.equals("1")) {
            throw new MegabotException("Invalid task status: " + statusStr
                    + ". Expected 0 or 1");
        }

        boolean isDone = statusStr.equals(DONE_STATUS);

        // Validate description
        if (taskDescription.isEmpty()) {
            throw new MegabotException("Task description cannot be empty");
        }

        Task task = createTaskByType(taskType, taskDescription, parts);


        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Validates if the task type is supported.
     *
     * @param taskType the task type to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidTaskType(String taskType) {
        return "T".equals(taskType) || "D".equals(taskType) || "E".equals(taskType);
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
                String[] dateParts = parts[DATE_INDEX].split(" to ");
                LOGGER.info(dateParts[0]);
                LOGGER.info(dateParts[1]);
                if (dateParts.length >= 2) {
                    return new Event(description, dateParts[0], dateParts[1]);
                }
            }
            break;
        default:
            throw new MegabotException("An invalid task was found in the file!");
        }
        return null;
    }
}
