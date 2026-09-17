package judey.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import judey.exception.JudeyException;

/**
 * Provides shared date/time parsing and formatting logic used by {@link Deadline} and {@link Event}.
 * Extracted here to avoid duplicating the same parsing rules and formats in both task types.
 */
public final class DateTimeUtil {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private DateTimeUtil() {
        // Prevents instantiation; this class only exposes static utility methods.
    }

    public static LocalDateTime parse(String text) throws JudeyException {
        try {
            return LocalDateTime.parse(text);
        } catch (DateTimeParseException isoParseFailed) {
            try {
                return LocalDateTime.parse(text, INPUT_FORMAT);
            } catch (DateTimeParseException userFormatParseFailed) {
                throw new JudeyException("Please use date format: d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
            }
        }
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMAT);
    }
}