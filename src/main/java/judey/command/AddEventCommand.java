package judey.command;

import judey.exception.JudeyException;
import judey.storage.Storage;
import judey.task.Event;
import judey.task.Task;
import judey.tasklist.TaskList;
import judey.ui.Ui;

public class AddEventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;

    public AddEventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws JudeyException {
        Task task = new Event(description, from, to);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.getSize());
    }
}