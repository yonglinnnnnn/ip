package megabot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import megabot.exception.MegabotException;
import megabot.task.Deadline;
import megabot.task.Task;
import megabot.task.ToDo;


class StorageTest {
    private Storage storage;

    private final String filePath = "./data/test_task.txt";

    @BeforeEach
    void setUp() {
        File testFile = new File(filePath);
        storage = new Storage(testFile.getAbsolutePath());
    }

    @Test
    void load_nonExistentFile_returnsEmptyList() throws MegabotException {
        ArrayList<Task> tasks = storage.load();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void save_emptyList_createsEmptyFile() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        storage.save(tasks);

        // Verify file exists and is empty
        File testFile = new File(filePath);
        File parentDir = testFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        assertTrue(testFile.exists());
        assertEquals(0, testFile.length());
    }

    @Test
    void save_taskList_writesCorrectFormat() throws IOException, MegabotException {
        ArrayList<Task> tasks = new ArrayList<>();
        ToDo todo = new ToDo("read book");
        Deadline deadline = new Deadline("submit assignment", "2023-12-01");

        tasks.add(todo);
        tasks.add(deadline);

        storage.save(tasks);

        // Verify by loading back
        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(2, loadedTasks.size());

        assertEquals("read book", loadedTasks.get(0).getTask());
        assertFalse(loadedTasks.get(0).getIsDone());

        assertEquals("submit assignment", loadedTasks.get(1).getTask());
        assertFalse(loadedTasks.get(1).getIsDone());
    }
}
