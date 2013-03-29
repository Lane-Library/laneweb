package edu.stanford.irt.laneweb.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LDAPDataTest {

    private LDAPData data;

    @Before
    public void setUp() throws Exception {
        this.data = new LDAPData("sunetid", "name", "univid", true, "email");
    }

    @Test
    public void testGetEmailAddress() {
        assertEquals("email", this.data.getEmailAddress());
    }

    @Test
    public void testGetName() {
        assertEquals("name", this.data.getName());
    }

    @Test
    public void testGetSunetId() {
        assertEquals("sunetid", this.data.getSunetId());
    }

    @Test
    public void testGetUnivId() {
        assertEquals("univid", this.data.getUnivId());
    }

    @Test
    public void testIsActive() {
        assertTrue(this.data.isActive());
    }

    @Test
    public void testToString() {
        assertEquals("sunetid=sunetid,univid=univid,name=name,isActive=trueemail=email", this.data.toString());
    }
}
