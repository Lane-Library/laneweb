package edu.stanford.irt.laneweb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LanewebExceptionTest {

    @Test
    public void testLanewebExceptionString() {
        assertEquals("oopsie", new LanewebException("oopsie").getMessage());
    }

    @Test
    public void testLanewebExceptionStringThrowable() {
        assertEquals("oopsie; nested exception is java.lang.RuntimeException: omg",
                new LanewebException("oopsie", new RuntimeException("omg")).getMessage());
    }

    @Test
    public void testLanewebExceptionThrowable() {
        assertEquals("java.lang.Exception: oopsie; nested exception is java.lang.Exception: oopsie",
                new LanewebException(new Exception("oopsie")).getMessage());
    }
}
