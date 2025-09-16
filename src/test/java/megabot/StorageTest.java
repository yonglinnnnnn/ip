package megabot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import megabot.exception.MegabotException;
import megabot.task.Deadline;
import megabot.task.Task;
import megabot.task.ToDo;


class StorageTest {
    private Storage storage;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        File testFile = tempDir.resolve("test_tasks.txt").toFile();
        storage = new Storage(testFile.getAbsolutePath());
    }

    @Test
    void load_nonExistentFile_returnsEmptyList() throws MegabotException {
        ArrayList<Task> tasks = storage.load();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void load_invalidLines_skipsInvalidLines() throws IOException, MegabotException {
        File testFile = tempDir.resolve("test_invalid.txt").toFile();
        FileWriter writer = new FileWriter(testFile);
        writer.write("invalid line\n");
        writer.close();

        Storage testStorage = new Storage(testFile.getAbsolutePath());

        try {
            ArrayList<Task> result = testStorage.load();
            System.out.println("Load completed successfully with " + result.size() + " tasks");
            fail("Expected InvalidTaskException to be thrown");
        } catch (MegabotException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }

    @Test
    void save_emptyList_createsEmptyFile() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        storage.save(tasks);

        // Verify file exists and is empty
        File testFile = tempDir.resolve("test_tasks.txt").toFile();
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

    @Test
    void save_createsDirectoryIfNotExists() throws IOException {
        File nonExistentDir = tempDir.resolve("new_dir").resolve("test.txt").toFile();
        Storage newStorage = new Storage(nonExistentDir.getAbsolutePath());

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new ToDo("test task"));

        // Should not throw exception
        assertDoesNotThrow(() -> newStorage.save(tasks));
        assertTrue(nonExistentDir.exists());
    }

    @Test
    void constructor_validFilePath_createsStorage() {
        Storage newStorage = new Storage("test/path/file.txt");
        assertNotNull(newStorage);
    }
}
