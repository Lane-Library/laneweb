package edu.stanford.irt.laneweb.cocoon.source;

import org.junit.Test;

public class LanewebSourceExceptionTest {

    @Test
    public void testLanewebSourceException() {
        new LanewebSourceException(new RuntimeException());
    }
}
