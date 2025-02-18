package edu.stanford.irt.laneweb;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class LanewebTest {

    @Test
    public void testServletContainer() {
        assertNotNull(new Laneweb().webServerFactory(8009));
    }
}
