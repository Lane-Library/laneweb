package edu.stanford.irt.laneweb;

public class LanewebException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LanewebException(String message) {
        super(message);
    }

    public LanewebException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LanewebException(Throwable cause) {
        super(cause);
    }
}
