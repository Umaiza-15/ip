package judey.tasklist;

import judey.exception.JudeyException;
import judey.task.Task;
import judey.task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
