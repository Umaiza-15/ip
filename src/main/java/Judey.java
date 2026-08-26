import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** A console chatbot that manages a small in-memory list of tasks. */
public class Judey {
    private static final String DIVIDER = "----------------------------------------";
    /** Location, relative to the project root, where task data is stored. */
    private static final Path SAVE_FILE = Path.of("data", "duke.txt");

    /** Starts the chatbot and continues processing commands until the user says goodbye. */
    public static void main(String[] args) {
        printWelcome();
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = loadTasks();

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

    /**
     * Loads saved tasks from the hard disk. Returns an empty list if the file or directory does not exist yet.
     *
     * @return list of loaded tasks
     */
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(SAVE_FILE)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(SAVE_FILE, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    Task task = parseTaskLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (JudeyException e) {
                    System.out.println("Warning: Skipping corrupted save line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not read save file at " + SAVE_FILE);
        }
        return tasks;
    }

    /**
     * Parses a single pipe-separated line from the save file into a Task object.
     *
     * @param line saved task record from disk
     * @return constructed Task object
     * @throws JudeyException if line formatting is invalid
     */
    private static Task parseTaskLine(String line) throws JudeyException {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new JudeyException("Invalid line format.");
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new JudeyException("Missing due date for deadline.");
                }
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new JudeyException("Missing time details for event.");
                }
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                throw new JudeyException("Unknown task type.");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
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
            case "events-on":
                printTasksOnDate(commandAndArgument, tasks);
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
                throw new JudeyException("Hmm, that command is still a mystery to me. Try todo, deadline, event, list, events-on, mark, unmark, delete, or bye.");
        }
    }

    /** Adds a todo after checking that it has a description. */
    private static void addTodo(String[] commandAndArgument, List<Task> tasks) throws JudeyException {
        if (commandAndArgument.length < 2 || commandAndArgument[1].isBlank()) {
            throw new JudeyException("Your todo is missing its mission! Try: todo read book");
        }
        Task task = new Todo(commandAndArgument[1]);
        tasks.add(task);
        saveTasks(tasks);
        printTaskAdded(task, tasks.size());
    }

    /** Adds a deadline after validating its description and /by date. */
    private static void addDeadline(String input, List<Task> tasks) throws JudeyException {
        String[] parts = input.split("\\s*/by\\s*", 2);
        if (parts.length != 2 || !parts[0].startsWith("deadline ") || parts[0].substring(9).isBlank() || parts[1].isBlank()) {
            throw new JudeyException("That deadline needs a date! Try: deadline report /by 2/12/2019 1800");
        }
        Task task = new Deadline(parts[0].substring(9).trim(), parts[1].trim());
        tasks.add(task);
        saveTasks(tasks);
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
        saveTasks(tasks);
        printTaskAdded(task, tasks.size());
    }

    /**
     * Prints all deadlines or events occurring on the given date.
     *
     * @param commandAndArgument split array containing the search date
     * @param tasks list of tasks to filter
     * @throws JudeyException if no date is provided or format is invalid
     */
    private static void printTasksOnDate(String[] commandAndArgument, List<Task> tasks) throws JudeyException {
        if (commandAndArgument.length < 2 || commandAndArgument[1].isBlank()) {
            throw new JudeyException("Please supply a date! Try: events-on 2/12/2019");
        }

        LocalDate searchDate;
        try {
            searchDate = LocalDate.parse(commandAndArgument[1].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (DateTimeParseException e) {
            throw new JudeyException("Invalid date format. Try: d/M/yyyy (e.g., 2/12/2019)");
        }

        System.out.println(DIVIDER);
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
        System.out.println(DIVIDER);
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
            saveTasks(tasks);
            System.out.println("Nice! I've marked this task as done: \n  " + tasks.get(taskNumber - 1));
        } else {
            tasks.get(taskNumber - 1).markAsNotDone();
            saveTasks(tasks);
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
        saveTasks(tasks);
        System.out.println(DIVIDER);
        System.out.println("Ok. I've removed this task:");
        System.out.print("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Writes the current task list to disk in a simple pipe-separated format.
     *
     * @param tasks task list to save
     * @throws JudeyException if the file cannot be written
     */
    private static void saveTasks(List<Task> tasks) throws JudeyException {
        List<String> taskRecords = new ArrayList<>();
        for (Task task : tasks) {
            taskRecords.add(task.toFileString());
        }

        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.write(SAVE_FILE, taskRecords, StandardCharsets.UTF_8);
        } catch (IOException error) {
            throw new JudeyException("I couldn't save your tasks to " + SAVE_FILE + ".");
        }
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