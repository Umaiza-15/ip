import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** A console chatbot that manages a small in-memory list of tasks. */
public class Judey {
    private static final String DIVIDER = "----------------------------------------";

    /** Starts the chatbot and continues processing commands until the user says goodbye. */
    public static void main(String[] args) {
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            try {
                if (input.equals("bye")) {
                    printGoodbye();
                    return;
                }
                processCommand(input, tasks);
            } catch (JudeyException error) {
                printError(error);
            }
        }
    }

    /** Processes one command. */
    private static void processCommand(String input, List<Task> tasks) throws JudeyException {
        String[] commandAndArgument = input.split("\\s+", 2);
        switch (commandAndArgument[0]) {
        case "todo":
            addTodo(commandAndArgument, tasks);
            return;
        case "deadline":
            addDeadline(input, tasks);
            return;
        case "event":
            addEvent(input, tasks);
            return;
        case "list":
            printList(tasks);
            return;
        case "mark":
            changeTaskStatus(commandAndArgument, tasks, true);
            return;
        case "unmark":
            changeTaskStatus(commandAndArgument, tasks, false);
            return;
        case "delete":
            deleteTask(commandAndArgument, tasks);
            return;
        default:
            throw new JudeyException("Hmm, that command is still a mystery to me. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    /** Adds a todo after checking that it has a description. */
    private static void addTodo(String[] commandAndArgument, List<Task> tasks) throws JudeyException {
        if (commandAndArgument.length < 2 || commandAndArgument[1].isBlank()) {
            throw new JudeyException("Your todo is missing its mission! Try: todo read book");
        }
        Task task = new Todo(commandAndArgument[1]);
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    /** Adds a deadline after validating its description and /by date. */
    private static void addDeadline(String input, List<Task> tasks) throws JudeyException {
        String[] parts = input.split("\\s*/by\\s*", 2);
        if (parts.length != 2 || !parts[0].startsWith("deadline ") || parts[0].substring(9).isBlank() || parts[1].isBlank()) {
            throw new JudeyException("That deadline needs a little more sparkle: deadline report /by Friday");
        }
        Task task = new Deadline(parts[0].substring(9).trim(), parts[1].trim());
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    /** Adds an event after validating its description, start time, and end time. */
    private static void addEvent(String input, List<Task> tasks) throws JudeyException {
        String[] descriptionAndRest = input.split("\\s*/from\\s*", 2);
        if (descriptionAndRest.length != 2 || !descriptionAndRest[0].startsWith("event ")) {
            throw new JudeyException("That event needs a name, /from time, and /to time to get on my calendar.");
        }
        String[] times = descriptionAndRest[1].split("\\s*/to\\s*", 2);
        String description = descriptionAndRest[0].substring(6).trim();
        if (times.length != 2 || description.isBlank() || times[0].isBlank() || times[1].isBlank()) {
            throw new JudeyException("That event needs a name, /from time, and /to time to get on my calendar.");
        }
        Task task = new Event(description, times[0].trim(), times[1].trim());
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    /** Marks or unmarks the requested task after validating its number. */
    private static void changeTaskStatus(String[] commandAndArgument, List<Task> tasks, boolean markDone)
            throws JudeyException {
        if (commandAndArgument.length < 2) {
            throw new JudeyException("I need a task number to aim at. Try: " + (markDone ? "mark 2" : "unmark 2"));
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandAndArgument[1]);
        } catch (NumberFormatException error) {
            throw new JudeyException("Task numbers are whole numbers only; no decimals or letters this time!");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new JudeyException("I checked twice, but that task number is not on the list.");
        }
        System.out.println(DIVIDER);
        if (markDone) {
            tasks.get(taskNumber - 1).markAsDone();
            System.out.println("Nice! I've marked this task as done: \n  " + tasks.get(taskNumber - 1));
        } else {
            tasks.get(taskNumber - 1).markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet: \n  " + tasks.get(taskNumber - 1));
        }
        System.out.println(DIVIDER);
    }

    /** Removes the requested task after validating its number. */
    private static void deleteTask(String[] commandAndArgument, List<Task> tasks) throws JudeyException {
        if (commandAndArgument.length < 2) {
            throw new JudeyException("I need a task number to delete. Try: delete 2");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandAndArgument[1]);
        } catch (NumberFormatException error) {
            throw new JudeyException("Task numbers are whole numbers only; no decimals or letters this time!");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new JudeyException("I checked twice, but that task number is not on the list.");
        }

        Task removedTask = tasks.remove(taskNumber - 1);
        System.out.println(DIVIDER);
        System.out.println("Noted. I've removed this task:");
        System.out.print("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Prints the current list of tasks. */
    private static void printList(List<Task> tasks) {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list: ");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.print((index + 1) + "." + tasks.get(index));
        }
        System.out.println();
        System.out.println(DIVIDER);
    }

    /** Prints confirmation after a task is added. */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task \n  " + task);
        System.out.println("Now you have " + taskCount + " tasks in this list.\n");
        System.out.println(DIVIDER);
    }

    /** Prints the greeting displayed when the program starts. */
    private static void printWelcome() {
        System.out.println(DIVIDER);
        String banner = "JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy\n"
                + "   JJ   uu   uu  dd   dd  ee       yy   yy\n"
                + "   JJ   uu   uu  dd   dd  eeeee     yyyyy\n"
                + "JJ JJ   uu   uu  dd   dd  ee         yyy\n"
                + " JJJ     uuuu u  dddddd   eeeeeee    yyy";
        System.out.println(banner + "\n");
        System.out.println("Hello! I'm Judey.\nWhat can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Prints a user-facing error message. */
    private static void printError(JudeyException error) {
        System.out.println(DIVIDER);
        System.out.println("Oopsie! " + error.getMessage());
        System.out.println(DIVIDER);
    }

    /** Prints the farewell displayed when the user exits. */
    private static void printGoodbye() {
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
