package megabot.exception;

public class InvalidTaskException extends Exception{

    public InvalidTaskException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}
