package judey.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import judey.command.AddDeadlineCommand;
import judey.command.AddEventCommand;
import judey.command.AddTodoCommand;
import judey.command.Command;
import judey.command.DeleteCommand;
import judey.command.EventsOnCommand;
import judey.command.ExitCommand;
import judey.command.HelpCommand;
import judey.command.ListCommand;
import judey.command.MarkCommand;
import judey.exception.JudeyException;

public class Parser {
    private static final DateTimeFormatter EVENTS_ON_DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy");

    public static Command parse(String fullCommand) throws JudeyException {
        String[] parts = fullCommand.trim().split("\\s+", 2);
        String commandWord = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
            case "bye":
                return new ExitCommand();
            case "help":
                if (!args.isBlank()) {
                    throw new JudeyException("The help command does not take any arguments. Try: help");
                }
                return new HelpCommand();
            case "list":
                return new ListCommand();
            case "todo":
                return parseTodo(args);
            case "deadline":
                return parseDeadline(args);
            case "event":
                return parseEvent(args);
            case "events-on":
                return parseEventsOn(args);
            case "mark":
                return new MarkCommand(parseIndex(args), true);
            case "unmark":
                return new MarkCommand(parseIndex(args), false);
            case "delete":
                return new DeleteCommand(parseIndex(args));
            default:
                throw new JudeyException("Hmm, that command is still a mystery to me. "
                        + "Try todo, deadline, event, list, events-on, mark, unmark, delete, help, or bye.");
        }
    }

    private static Command parseTodo(String args) throws JudeyException {
        if (args.isBlank()) {
            throw new JudeyException("Your todo is missing its mission! Try: todo read book");
        }
        return new AddTodoCommand(args.trim());
    }

    private static Command parseDeadline(String args) throws JudeyException {
        String usage = "That deadline needs a date! Try: deadline report /by 2/12/2019 1800";
        if (args.isBlank()) {
            throw new JudeyException(usage);
        }
        String[] deadlineParts = args.split("\\s*/by\\s*", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new JudeyException(usage);
        }
        return new AddDeadlineCommand(deadlineParts[0].trim(), deadlineParts[1].trim());
    }

    private static Command parseEvent(String args) throws JudeyException {
        String usage = "That event needs a name, /from time, and /to time to get on my calendar.";
        if (args.isBlank()) {
            throw new JudeyException(usage);
        }
        String[] descAndFrom = args.split("\\s*/from\\s*", 2);
        if (descAndFrom.length < 2 || descAndFrom[0].isBlank()) {
            throw new JudeyException(usage);
        }
        String[] fromAndTo = descAndFrom[1].split("\\s*/to\\s*", 2);
        if (fromAndTo.length < 2 || fromAndTo[0].isBlank() || fromAndTo[1].isBlank()) {
            throw new JudeyException(usage);
        }
        return new AddEventCommand(descAndFrom[0].trim(), fromAndTo[0].trim(), fromAndTo[1].trim());
    }

    private static Command parseEventsOn(String args) throws JudeyException {
        if (args.isBlank()) {
            throw new JudeyException("Please supply a date! Try: events-on 2/12/2019");
        }
        try {
            LocalDate date = LocalDate.parse(args.trim(), EVENTS_ON_DATE_FORMAT);
            return new EventsOnCommand(date);
        } catch (DateTimeParseException e) {
            throw new JudeyException("Invalid date format. Try: d/M/yyyy (e.g., 2/12/2019)");
        }
    }

    private static int parseIndex(String args) throws JudeyException {
        if (args.isBlank()) {
            throw new JudeyException("I need a valid task number to perform that command.");
        }
        try {
            return Integer.parseInt(args.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new JudeyException("Task numbers are whole numbers only; "
                    + "no decimals or letters this time!");
        }
    }
}
