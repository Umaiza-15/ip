package judey.parser;

import judey.command.*;
import judey.exception.JudeyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @Test
    public void parseCommand_validExitCommand_returnsExitCommand() throws Exception {
        Command command = Parser.parse("bye");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parseCommand_validListCommandWithExtraSpaces_returnsListCommand() throws Exception {
        Command command = Parser.parse("   list   ");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parseCommand_todoWithDescription_returnsAddCommand() throws Exception {
        Command command = Parser.parse("todo read book");
        assertInstanceOf(AddTodoCommand.class, command);
    }

    @Test
    public void parseCommand_todoMissingDescription_exceptionThrown() {
        JudeyException exception = assertThrows(
                JudeyException.class,
                () -> Parser.parse("todo")
        );
        assertEquals("Your todo is missing its mission! Try: todo read book", exception.getMessage());
    }

    @Test
    public void parseCommand_unknownCommand_exceptionThrown() {
        assertThrows(
                JudeyException.class,
                () -> Parser.parse("invalidCommand 123")
        );
    }

    // Deadline tasks
    @Test
    public void parseCommand_validDeadline_returnsAddDeadlineCommand() throws Exception {
        Command command = Parser.parse("deadline submit assignment /by 2026-12-31 2359");
        assertInstanceOf(AddDeadlineCommand.class, command);
    }

    @Test
    public void parseCommand_deadlineMissingDescription_exceptionThrown() {
        JudeyException exception = assertThrows(
                JudeyException.class,
                () -> Parser.parse("deadline /by 2026-12-31 2359")
        );
        assertEquals("That deadline needs a date! Try: deadline report /by 2/12/2019 1800", exception.getMessage());
    }

    @Test
    public void parseCommand_deadlineMissingByFlag_exceptionThrown() {
        JudeyException exception = assertThrows(
                JudeyException.class,
                () -> Parser.parse("deadline submit assignment")
        );
        assertEquals("That deadline needs a date! Try: deadline report /by 2/12/2019 1800", exception.getMessage());
    }

    @Test
    public void parseCommand_deadlineEmptyByValue_exceptionThrown() {
        JudeyException exception = assertThrows(
                JudeyException.class,
                () -> Parser.parse("deadline submit assignment /by ")
        );
        assertEquals("That deadline needs a date! Try: deadline report /by 2/12/2019 1800", exception.getMessage());
    }

    @Test
    public void parseCommand_deadlineInvalidDateFormat_exceptionThrown() {
        assertThrows(
                JudeyException.class,
                () -> Parser.parse("That deadline needs a date! Try: deadline report /by 2/12/2019 1800")
        );
    }

    //Event tests
    @Test
    public void parseCommand_validEvent_returnsAddEventCommand() throws Exception {
        Command command = Parser.parse("event team meeting /from 2026-10-15 1400 /to 2026-10-15 1600");
        assertInstanceOf(AddEventCommand.class, command);
    }

    @Test
    public void parseCommand_eventMissingDescription_exceptionThrown() {
        JudeyException exception = assertThrows(
                JudeyException.class,
                () -> Parser.parse("event /from 2026-10-15 1400 /to 2026-10-15 1600")
        );
        assertEquals("That event needs a name, /from time, and /to time to get on my calendar.", exception.getMessage());
    }

    @Test
    public void parseCommand_eventMissingToFlag_exceptionThrown() {
        JudeyException exception = assertThrows(
                JudeyException.class,
                () -> Parser.parse("event team meeting /from 2026-10-15 1400")
        );
        assertEquals( "That event needs a name, /from time, and /to time to get on my calendar.", exception.getMessage());
    }

    @Test
    public void parseCommand_eventSwappedFlags_exceptionThrown() {
        // Checking behavior when /to appears before /from
        assertThrows(
                JudeyException.class,
                () -> Parser.parse("event team meeting /to 2026-10-15 1600 /from 2026-10-15 1400")
        );
    }

    @Test
    public void parseCommand_eventEmptyFromValue_exceptionThrown() {
        assertThrows(
                JudeyException.class,
                () -> Parser.parse("event team meeting /from  /to 2026-10-15 1600")
        );
    }
}