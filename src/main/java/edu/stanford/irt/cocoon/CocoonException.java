package edu.stanford.irt.cocoon;

public class CocoonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CocoonException(final String message) {
        super(message);
    }

    public CocoonException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CocoonException(final Throwable cause) {
        super(cause);
    }
}
