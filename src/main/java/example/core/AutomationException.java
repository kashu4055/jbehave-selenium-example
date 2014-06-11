package example.core;

public class AutomationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AutomationException() {
    }

    public AutomationException(String message) {
        super(message);
    }

    public AutomationException(Throwable cause) {
        super(cause);
    }

    public AutomationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutomationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
