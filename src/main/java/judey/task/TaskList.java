package judey.task;

import judey.exception.JudeyException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Manages the task list and provides operations to modify tasks. */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getSize() {
        return tasks.size();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) throws JudeyException {
        validateIndex(index);
        return tasks.remove(index);
    }

    public Task markAsDone(int index) throws JudeyException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    public Task markAsNotDone(int index) throws JudeyException {
        validateIndex(index);
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    private void validateIndex(int index) throws JudeyException {
        if (index < 0 || index >= tasks.size()) {
            throw new JudeyException("I checked twice, but that task number is not on the list.");
        }
    }

    public void printList() {
        System.out.println("Here are the tasks in your list: ");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.print((index + 1) + "." + tasks.get(index));
        }
        System.out.println();
    }

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