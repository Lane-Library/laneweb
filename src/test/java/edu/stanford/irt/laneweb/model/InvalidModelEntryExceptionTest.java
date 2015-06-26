package edu.stanford.irt.laneweb.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InvalidModelEntryExceptionTest {

    @Test
    public void testInvalidModelEntryException() {
        assertEquals("invalid model entry: key=key, value=value",
                new InvalidModelEntryException("key", "value").getMessage());
    }
}
