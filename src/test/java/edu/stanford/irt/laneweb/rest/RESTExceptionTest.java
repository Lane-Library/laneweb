package edu.stanford.irt.laneweb.rest;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.web.client.RestClientException;

public class RESTExceptionTest {

    @Test
    public void test() {
        RestClientException e = new RestClientException("oopsie");
        assertSame(e, new RESTException(e).getCause());
    }
}
