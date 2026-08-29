package judey.task;

import judey.exception.JudeyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline date and time
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime by;

    /**
     * Constructs a {@code Deadline} task with a description and target date/time string
     * @param desc The description of the deadline task
     * @param by The date and time by which the task should be completed by, parsed into a {@code LocalDateTime}
     * @throws JudeyException If the given date/time string cannot be parsed using supported formats
     */
    public Deadline(String desc, String by) throws JudeyException {
        super(desc);
        this.by = parseDateTime(by);
    }

    /**
     * Parses a text string into a {@code LocalDateTime} object using d/M/yyyy HHmm formatting
     * @param text the date/time to be parsed
     * @return the parsed {@code LocalDateTime} object
     * @throws JudeyException If the text does not match any valid date/time format
     */
    private LocalDateTime parseDateTime(String text) throws JudeyException {
        try {
            return LocalDateTime.parse(text);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDateTime.parse(text, INPUT_FORMAT);
            } catch (DateTimeParseException e2) {
                throw new JudeyException("Please use date format: d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
            }
        }
    }

    /**
     * Getter for the date and time by which the task must be completed by
     * @return {@code LocalDateTime} due date
     */
    public LocalDateTime getBy() {
        return this.by;
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")\n";
    }
}