package edu.stanford.irt.laneweb.rest;

import java.io.IOException;

import org.springframework.web.client.RestClientException;

import edu.stanford.irt.laneweb.LanewebException;

public class RESTException extends LanewebException {

    private static final long serialVersionUID = 1L;

    public RESTException(final IOException cause) {
        super(cause);
    }

    public RESTException(final RestClientException cause) {
        super(cause);
    }
}
