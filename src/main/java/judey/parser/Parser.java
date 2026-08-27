package judey.parser;

import judey.command.*;
import judey.exception.JudeyException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Parses user input into concrete judey.command.Command objects. */
public class Parser {

    /**
     * Parses the full user input command string.
     *
     * @param fullCommand raw command entered by user
     * @return executable judey.command.Command instance
     * @throws JudeyException if input format or command name is invalid
     */
    public static Command parse(String fullCommand) throws JudeyException {
        String[] parts = fullCommand.trim().split("\\s+", 2);
        String commandWord = parts[0];

        switch (commandWord) {
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "todo":
                if (parts.length < 2 || parts[1].isBlank()) {
                    throw new JudeyException("Your todo is missing its mission! Try: todo read book");
                }
                return new AddTodoCommand(parts[1].trim());

            case "deadline":
                if (parts.length < 2) {
                    throw new JudeyException("That deadline needs a date! Try: deadline report /by 2/12/2019 1800");
                }
                String[] deadlineParts = parts[1].split("\\s*/by\\s*", 2);
                if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
                    throw new JudeyException("That deadline needs a date! Try: deadline report /by 2/12/2019 1800");
                }
                return new AddDeadlineCommand(deadlineParts[0].trim(), deadlineParts[1].trim());

            case "event":
                if (parts.length < 2) {
                    throw new JudeyException("That event needs a name, /from time, and /to time to get on my calendar.");
                }
                String[] eventDescParts = parts[1].split("\\s*/from\\s*", 2);
                if (eventDescParts.length < 2 || eventDescParts[0].isBlank()) {
                    throw new JudeyException("That event needs a name, /from time, and /to time to get on my calendar.");
                }
                String[] timeParts = eventDescParts[1].split("\\s*/to\\s*", 2);
                if (timeParts.length < 2 || timeParts[0].isBlank() || timeParts[1].isBlank()) {
                    throw new JudeyException("That event needs a name, /from time, and /to time to get on my calendar.");
                }
                return new AddEventCommand(eventDescParts[0].trim(), timeParts[0].trim(), timeParts[1].trim());

            case "events-on":
                if (parts.length < 2 || parts[1].isBlank()) {
                    throw new JudeyException("Please supply a date! Try: events-on 2/12/2019");
                }
                try {
                    LocalDate date = LocalDate.parse(parts[1].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
                    return new EventsOnCommand(date);
                } catch (DateTimeParseException e) {
                    throw new JudeyException("Invalid date format. Try: d/M/yyyy (e.g., 2/12/2019)");
                }

            case "mark":
                return new MarkCommand(parseIndex(parts), true);

            case "unmark":
                return new MarkCommand(parseIndex(parts), false);

            case "delete":
                return new DeleteCommand(parseIndex(parts));

            default:
                throw new JudeyException("Hmm, that command is still a mystery to me. Try todo, deadline, event, list, events-on, mark, unmark, delete, or bye.");
        }
    }

    private static int parseIndex(String[] parts) throws JudeyException {
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new JudeyException("I need a valid task number to perform that command.");
        }
        try {
            return Integer.parseInt(parts[1].trim()) - 1;
        } catch (NumberFormatException e) {
            throw new JudeyException("judey.task.Task numbers are whole numbers only; no decimals or letters this time!");
        }
    }
}