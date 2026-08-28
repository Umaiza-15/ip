package judey.command;

import judey.exception.JudeyException;
import judey.storage.Storage;
import judey.task.Deadline;
import judey.task.Task;
import judey.tasklist.TaskList;
import judey.ui.Ui;

public class AddDeadlineCommand extends Command {
    private final String description;
    private final String by;

    public AddDeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JudeyException {
        Task task = new Deadline(description, by);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.getSize());
    }
}