package judey.ui;

import judey.task.Task;

import java.util.List;
import java.util.Scanner;

/** Handles interactions with the user, including reading inputs and printing outputs. */
public class Ui {
    private static final String DIVIDER = "----------------------------------------";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Reads the next line of user input. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Checks if there is another command available. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Prints a visual divider line. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Prints the startup welcome message and banner. */
    public void showWelcome() {
        showLine();
        String banner = "JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy\n"
                + "   JJ   uu   uu  dd   dd  ee       yy   yy\n"
                + "   JJ   uu   uu  dd   dd  eeeee     yyyyy\n"
                + "JJ JJ   uu   uu  dd   dd  ee         yyy\n"
                + " JJJ     uuuu u  dddddd   eeeeeee    yyy";
        System.out.println(banner + "\n");
        System.out.println("Hello! I'm judey.Judey.\nWhat can I do for you?");
        showLine();
    }

    /** Prints the farewell message. */
    public void showGoodbye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /** Prints a user-facing error message. */
    public void showError(String message) {
        showLine();
        System.out.println("Oopsie! " + message);
        showLine();
    }

    /** Prints an error message when storage fails to load data. */
    public void showLoadingError() {
        showLine();
        System.out.println("Warning: Could not load saved tasks. Starting with an empty task list.");
        showLine();
    }

    /** Prints confirmation after a task is added. */
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("Got it. I've added this task \n  " + task);
        System.out.println("Now you have " + taskCount + " tasks in this list.\n");
        showLine();
    }

    /** Prints confirmation after a task is removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println("Ok. I've removed this task:");
        System.out.print("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /** Prints notification when a task is marked or unmarked. */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        showLine();
        if (isDone) {
            System.out.println("Nice! I've marked this task as done: \n  " + task);
        } else {
            System.out.println("OK, I've marked this task as not done yet: \n  " + task);
        }
        showLine();
    }
}
