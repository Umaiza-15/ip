package judey.command;

import judey.storage.Storage;
import judey.task.TaskList;
import judey.ui.Ui;

import java.time.LocalDate;

public class EventsOnCommand extends Command {
    private final LocalDate date;

    public EventsOnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.printTasksOnDate(date);
    }
}