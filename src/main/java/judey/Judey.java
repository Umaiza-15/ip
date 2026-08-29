package judey;

import judey.command.Command;
import judey.exception.JudeyException;
import judey.parser.Parser;
import judey.storage.Storage;
import judey.tasklist.TaskList;
import judey.ui.Ui;

/** Main entry point for the judey.Judey task manager application. */
public class Judey {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Creates an instance of judey with a file path where the list of tasks would be stored
     * @param filePath location that the list of tasks is stored to
     */
    public Judey(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (JudeyException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /** Runs the chatbot main loop. */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (JudeyException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Judey("data/duke.txt").run();
    }
}