import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime by;

    public Deadline(String desc, String by) throws JudeyException {
        super(desc);
        this.by = parseDateTime(by);
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

    public LocalDateTime getBy() {
        return this.by;
    }

    /**
     * Returns this deadline in the pipe-separated format used by Judey's save file.
     *
     * @return deadline type, completion status, description, and due date
     */
    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")\n";
    }
}