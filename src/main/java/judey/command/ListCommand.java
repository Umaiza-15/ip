package judey.command;

import judey.storage.Storage;
import judey.task.TaskList;
import judey.ui.Ui;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.printList();
    }
}