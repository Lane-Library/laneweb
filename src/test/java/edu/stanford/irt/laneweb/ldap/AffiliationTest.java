package edu.stanford.irt.laneweb.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AffiliationTest {

    @Test
    public void testGetAffiliation() {
        assertNotNull(Affiliation.getAffiliation("stanford:staff"));
    }

    @Test
    public void testGetDisplay() {
        assertEquals("Staff (Active)", Affiliation.getAffiliation("stanford:staff").getDisplay());
    }

    @Test
    public void testGetLdapAffiliateString() {
        assertEquals("stanford:staff", Affiliation.getAffiliation("stanford:staff").getLdapAffiliateString());
    }

    @Test
    public void testIsActive() {
        assertTrue(Affiliation.getAffiliation("stanford:staff").isActive());
    }

    @Test
    public void testNotFoune() {
        assertEquals(Affiliation.AffiliationNotFound, Affiliation.getAffiliation("foo"));
    }
}
