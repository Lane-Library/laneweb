package edu.stanford.irt.laneweb.rest;

import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.Test;
import org.springframework.web.client.RestClientException;

public class RESTExceptionTest {

    @Test
    public void testIOException() {
        IOException e = new IOException("oopsie");
        assertSame(e, new RESTException(e).getCause());
    }

    @Test
    public void testRestClientException() {
        RestClientException e = new RestClientException("oopsie");
        assertSame(e, new RESTException(e).getCause());
    }
}
