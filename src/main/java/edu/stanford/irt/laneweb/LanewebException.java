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

    @Override
    public String getMessage() {
        Throwable cause = getCause();
        String message = super.getMessage();
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("nested exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }
}
