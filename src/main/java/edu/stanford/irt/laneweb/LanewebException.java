package edu.stanford.irt.laneweb;

public class LanewebException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LanewebException(final String message) {
        super(message);
    }

    public LanewebException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LanewebException(final Throwable cause) {
        super(cause);
    }
}
