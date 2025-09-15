package megabot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import megabot.task.Task;
import megabot.task.TaskList;
import megabot.task.ToDo;


class TaskListTest {
    private TaskList taskList;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        task1 = new ToDo("task 1");
        task2 = new ToDo("task 2");
    }

    @Test
    void constructor_empty_createsEmptyTaskList() {
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.size());
    }

    @Test
    void constructor_withExistingList_createsTaskListCorrectly() {
        ArrayList<Task> existingTasks = new ArrayList<>();
        existingTasks.add(task1);
        existingTasks.add(task2);

        TaskList newTaskList = new TaskList(existingTasks);
        assertEquals(2, newTaskList.size());
        assertFalse(newTaskList.isEmpty());
        assertEquals(task1, newTaskList.getTask(0));
        assertEquals(task2, newTaskList.getTask(1));
    }

    @Test
    void addTask_validTask_addsTaskCorrectly() {
        taskList.addTask(task1);
        assertEquals(1, taskList.size());
        assertFalse(taskList.isEmpty());
        assertEquals(task1, taskList.getTask(0));
    }

    @Test
    void addTask_multipleTasks_addsAllTasksCorrectly() {
        taskList.addTask(task1);
        taskList.addTask(task2);
        assertEquals(2, taskList.size());
        assertEquals(task1, taskList.getTask(0));
        assertEquals(task2, taskList.getTask(1));
    }

    @Test
    void getTask_validIndex_returnsCorrectTask() {
        taskList.addTask(task1);
        taskList.addTask(task2);

        assertEquals(task1, taskList.getTask(0));
        assertEquals(task2, taskList.getTask(1));
    }

    @Test
    void getTask_invalidIndex_returnsNull() {
        taskList.addTask(task1);

        assertNull(taskList.getTask(-1));
        assertNull(taskList.getTask(5));
    }

    @Test
    void markTask_validIndex_marksTaskCorrectly() {
        taskList.addTask(task1);
        assertFalse(task1.getIsDone());

        taskList.markTask(0);
        assertTrue(task1.getIsDone());
    }

    @Test
    void markTask_invalidIndex_doesNothing() {
        taskList.addTask(task1);
        assertFalse(task1.getIsDone());

        taskList.markTask(-1);
        taskList.markTask(5);
        assertFalse(task1.getIsDone());
    }

    @Test
    void unmarkTask_validIndex_unmarksTaskCorrectly() {
        taskList.addTask(task1);
        task1.markAsDone();
        assertTrue(task1.getIsDone());

        taskList.unmarkTask(0);
        assertFalse(task1.getIsDone());
    }

    @Test
    void unmarkTask_invalidIndex_doesNothing() {
        taskList.addTask(task1);
        task1.markAsDone();
        assertTrue(task1.getIsDone());

        taskList.unmarkTask(-1);
        taskList.unmarkTask(5);
        assertTrue(task1.getIsDone());
    }

    @Test
    void isValidIndex_validIndices_returnsTrue() {
        taskList.addTask(task1);
        taskList.addTask(task2);

        assertTrue(taskList.isValidIndex(0));
        assertTrue(taskList.isValidIndex(1));
    }

    @Test
    void isValidIndex_invalidIndices_returnsFalse() {
        taskList.addTask(task1);

        assertFalse(taskList.isValidIndex(-1));
        assertFalse(taskList.isValidIndex(1));
        assertFalse(taskList.isValidIndex(5));
    }

    @Test
    void getTasks_returnsCorrectList() {
        taskList.addTask(task1);
        taskList.addTask(task2);

        ArrayList<Task> tasks = taskList.getTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }
}
