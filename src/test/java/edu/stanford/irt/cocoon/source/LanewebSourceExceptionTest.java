package edu.stanford.irt.cocoon.source;

import org.junit.Test;

import edu.stanford.irt.cocoon.source.SourceException;

public class LanewebSourceExceptionTest {

    @Test
    public void testLanewebSourceException() {
        new SourceException(new RuntimeException());
    }
}
