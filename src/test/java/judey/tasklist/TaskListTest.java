package judey.tasklist;

import judey.exception.JudeyException;
import judey.task.Task;
import judey.task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {

    private TaskList taskList;
    private Task task1;
    private Task task2;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        task1 = new Todo("Read book");
        task2 = new Todo("Return book");
        taskList.add(task1);
        taskList.add(task2);
    }

    @Test
    public void deleteTask_validFirstIndex_taskRemovedAndSizeDecreased() throws Exception {
        Task removedTask = taskList.delete(0);

        assertEquals(task1, removedTask);
        assertEquals(1, taskList.getSize());
        assertEquals(task2, taskList.getTasks().get(0));
    }

    @Test
    public void deleteTask_validLastIndex_taskRemovedSuccessfully() throws Exception {
        Task removedTask = taskList.delete(1);

        assertEquals(task2, removedTask);
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void deleteTask_negativeIndex_exceptionThrown() {
        assertThrows(
                JudeyException.class,
                () -> taskList.delete(-1)
        );
    }

    @Test
    public void deleteTask_indexOutOfBounds_exceptionThrown() {
        assertThrows(
                JudeyException.class,
                () -> taskList.delete(2)
        );
    }

    @Test
    public void deleteTask_emptyList_exceptionThrown() {
        TaskList emptyList = new TaskList();
        assertThrows(
                JudeyException.class,
                () -> emptyList.delete(0)
        );
    }

    @Test
    public void findTasks_caseInsensitiveMultiWordSearch_preservesOriginalTaskNumbers() {
        taskList.getTasks().add(1, new Todo("Buy milk"));
        taskList.add(new Todo("Read book"));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));
        try {
            taskList.findTasks("READ BOOK");
        } finally {
            System.setOut(originalOut);
        }

        String lineSeparator = System.lineSeparator();
        assertEquals("Here are the matching tasks in your mission log:" + lineSeparator
                + "1.[T][ ] Read book\n"
                + "4.[T][ ] Read book\n", output.toString());
    }

    @Test
    public void findTasks_noMatch_printsMissionMessage() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));
        try {
            taskList.findTasks("spaceship");
        } finally {
            System.setOut(originalOut);
        }

        String lineSeparator = System.lineSeparator();
        assertEquals("Here are the matching tasks in your mission log:" + lineSeparator
                + "No matching tasks found in this sector." + lineSeparator, output.toString());
    }
}
