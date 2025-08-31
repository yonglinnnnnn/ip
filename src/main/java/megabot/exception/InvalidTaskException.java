package megabot.exception;

/**
 * Exception thrown when there is an error with task creation, parsing, or manipulation.
 * This custom exception is used throughout the MegaBot application to handle
 * task-related errors in a consistent manner.
 *
 * @author Xu Yong Lin
 * @version 1.0
 */
public class InvalidTaskException extends Exception{
    /**
     * Constructs an InvalidTaskException with the specified error message.
     *
     * @param msg the error message describing what went wrong
     */
    public InvalidTaskException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}
