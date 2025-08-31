package megabot;

import megabot.exception.InvalidTaskException;
import megabot.task.Deadline;
import megabot.task.Event;
import megabot.task.Task;
import megabot.task.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }
}