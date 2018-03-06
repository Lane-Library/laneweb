package edu.stanford.irt.laneweb;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LanewebTest {

    @Test
    public void testServletContainer() {
        assertNotNull(new Laneweb().webServerFactory(8009));
    }
}
