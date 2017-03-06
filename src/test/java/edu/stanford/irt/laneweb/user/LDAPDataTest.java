package edu.stanford.irt.laneweb.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LDAPDataTest {

    private LDAPData data;

    @Before
    public void setUp() throws Exception {
        this.data = new LDAPData("sunetid", true);
    }

    @Test
    public void testGetSunetId() {
        assertEquals("sunetid", this.data.getSunetId());
    }

    @Test
    public void testIsActive() {
        assertTrue(this.data.isActive());
    }
}
