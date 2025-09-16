package megabot.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InvalidTaskExceptionTest {

    @Test
    void constructor_validMessage_createsExceptionCorrectly() {
        String message = "Test error message";
        MegabotException exception = new MegabotException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.toString());
    }

    @Test
    void constructor_emptyMessage_createsExceptionCorrectly() {
        String message = "";
        MegabotException exception = new MegabotException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(message, exception.toString());
    }

    @Test
    void constructor_nullMessage_createsExceptionCorrectly() {
        MegabotException exception = new MegabotException(null);

        assertNull(exception.getMessage());
        assertNull(exception.toString());
    }

    @Test
    void toString_returnsMessageCorrectly() {
        String message = "OOPSIE!! Something went wrong";
        MegabotException exception = new MegabotException(message);

        assertEquals(message, exception.toString());
    }

    @Test
    void isInstanceOfException_returnsTrue() {
        MegabotException exception = new MegabotException("test");
        assertTrue(exception instanceof Exception);
    }
}
