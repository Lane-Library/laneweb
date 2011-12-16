package edu.stanford.irt.laneweb.cocoon.source;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LanewebSourceExceptionTest {
    

    @Test
    public void testLanewebSourceException() {
        new LanewebSourceException(new RuntimeException());
    }
}
