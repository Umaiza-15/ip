package judey.task;

import judey.exception.JudeyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with an event duration - start and end time
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs a {@code Event} task with a description and target date/time from and to
     * @param desc The description of the event task
     * @param from The date/time at which the event begins; will be parsed into {@code LocalDateTime}
     * @param to The date/time at which the event ends; will be parsed into {@code LocalDateTime}
     * @throws JudeyException If the given date/time string cannot be parsed using supported formats
     */
    public Event(String desc, String from, String to) throws JudeyException {
        super(desc);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
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
     * Getter for the date and time at which the event begins
     * @return {@code LocalDateTime} start time
     */
    public LocalDateTime getFrom() {
        return this.from;
    }

    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        String display = "[E]" + super.toString();
        display += " (from: " + this.from.format(OUTPUT_FORMAT) + " to: " + this.to.format(OUTPUT_FORMAT) + ")\n";
        return display;
    }
}