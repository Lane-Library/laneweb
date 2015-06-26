package edu.stanford.irt.laneweb.model;

import edu.stanford.irt.laneweb.LanewebException;

public class InvalidModelEntryException extends LanewebException {

    private static final long serialVersionUID = 1L;

    public InvalidModelEntryException(final String key, final Object value) {
        super(new StringBuilder("invalid model entry: key=").append(key).append(", value=").append(value).toString());
    }
}
