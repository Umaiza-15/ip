package judey.task;

import judey.exception.JudeyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String desc, String from, String to) throws JudeyException {
        super(desc);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
    }

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

    public LocalDateTime getFrom() {
        return this.from;
    }

    /**
     * Returns this event in the pipe-separated format used by judey.Judey's save file.
     *
     * @return event type, completion status, description, start time, and end time
     */
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