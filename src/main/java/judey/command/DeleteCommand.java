package judey.command;

import judey.exception.JudeyException;
import judey.storage.Storage;
import judey.task.Task;
import judey.tasklist.TaskList;
import judey.ui.Ui;

public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JudeyException {
        Task removedTask = tasks.delete(index);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(removedTask, tasks.getSize());
    }
}
