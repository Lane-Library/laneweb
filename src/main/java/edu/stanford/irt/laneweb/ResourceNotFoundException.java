package edu.stanford.irt.laneweb;

public class ResourceNotFoundException extends LanewebException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
