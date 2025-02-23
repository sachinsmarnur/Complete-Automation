package customException;

public class BrowserNotAvailable extends RuntimeException {

    private final String message;

    public BrowserNotAvailable(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return "Browser Not Available{" + "message=" + message + '\'' + '}';
    }
}
