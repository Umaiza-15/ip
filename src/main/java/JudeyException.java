/**
 * Represents an error caused by an invalid command entered into Judey.
 */
public class JudeyException extends Exception {
    /**
     * Creates an exception with a message that explains how the user can correct the command.
     *
     * @param message explanation of the invalid command
     */
    public JudeyException(String message) {
        super(message);
    }
}
