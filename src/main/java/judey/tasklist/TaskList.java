package judey.tasklist;

import judey.exception.JudeyException;
import judey.task.Deadline;
import judey.task.Event;
import judey.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Manages the task list and provides operations to modify tasks. */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a TaskList and intialises the list of tasks to be an empty list
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Create a TaskList with an existing list of tasks
     * @param tasks refers to an existing list of tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Getter for the tasks field
     * @return the list of tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Getter for the size of list of tasks
     * @return int representing the number of tasks in the list of tasks
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Add a new task to the list of tasks
     * @param task refers to the task to be added into the list
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Delete task from the list of tasks
     * @param index the task at this index would be deleted
     * @return the task that was removed
     * @throws JudeyException
     */
    public Task delete(int index) throws JudeyException {
        validateIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks a task as done
     * @param index task at this index of the list is to be marked as done
     * @return the task that is marked as done
     * @throws JudeyException if index is greater than the number of tasks in the list or is less than 0,
     * an exception is thrown in validateIndex method
     */
    public Task markAsDone(int index) throws JudeyException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Unmarks a task - task is not done
     * @param index task at this index of the list is to be marked as not done
     * @return the task that is marked as not done
     * @throws JudeyException if index is greater than the number of tasks in the list or is less than 0,
     * an exception is thrown in validateIndex method
     */
    public Task markAsNotDone(int index) throws JudeyException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Checks if the index is within valid range of the task list
     * @param index the index which is being validated
     * @throws JudeyException if the index is negative or greater than or equal to the total number of tasks
     */
    private void validateIndex(int index) throws JudeyException {
        if (index < 0 || index >= tasks.size()) {
            throw new JudeyException("I checked twice, but that task number is not on the list.");
        }
    }

    /**
     * Prints all tasks in the list sequentially with 1-based index numbering
     */
    public void printList() {
        System.out.println("Here are the tasks in your list: ");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.print((index + 1) + "." + tasks.get(index));
        }
        System.out.println();
    }

    /**
     * Filters and prints all deadlines or events on the specified date.
     * If no matching tasks are found, a notification is printed instead.
     *
     * @param searchDate The target date to search for tasks
     */
    public void printTasksOnDate(LocalDate searchDate) {
        System.out.println("Tasks on " + searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
        int count = 0;
        for (Task task : tasks) {
            boolean match = false;
            if (task instanceof Deadline) {
                match = ((Deadline) task).getBy().toLocalDate().equals(searchDate);
            } else if (task instanceof Event) {
                match = ((Event) task).getFrom().toLocalDate().equals(searchDate);
            }

            if (match) {
                count++;
                System.out.print(count + "." + task);
            }
        }
        if (count == 0) {
            System.out.println(" No deadlines or events found on this date.");
        }
    }
}