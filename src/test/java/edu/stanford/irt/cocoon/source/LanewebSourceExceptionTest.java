package edu.stanford.irt.cocoon.source;

import org.junit.Test;

public class LanewebSourceExceptionTest {

    @Test
    public void testLanewebSourceException() {
        new SourceException(new RuntimeException());
    }
}
