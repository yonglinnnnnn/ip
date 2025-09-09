package megabot.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InvalidTaskExceptionTest {

    @Test
    void constructor_validMessage_createsExceptionCorrectly() {
        String message = "Test error message";
        InvalidTaskException exception = new InvalidTaskException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.toString());
    }

    @Test
    void constructor_emptyMessage_createsExceptionCorrectly() {
        String message = "";
        InvalidTaskException exception = new InvalidTaskException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.toString());
    }

    @Test
    void constructor_nullMessage_createsExceptionCorrectly() {
        InvalidTaskException exception = new InvalidTaskException(null);

        assertNull(exception.getMessage());
        assertNull(exception.toString());
    }

    @Test
    void toString_returnsMessageCorrectly() {
        String message = "OOPSIE!! Something went wrong";
        InvalidTaskException exception = new InvalidTaskException(message);

        assertEquals(message, exception.toString());
    }

    @Test
    void isInstanceOfException_returnsTrue() {
        InvalidTaskException exception = new InvalidTaskException("test");
        assertTrue(exception instanceof Exception);
    }
}
