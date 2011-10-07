package edu.stanford.irt.laneweb;

public class LanewebException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LanewebException(Throwable cause) {
        super(cause);
    }

    public LanewebException(String message) {
        super(message);
    }
}
