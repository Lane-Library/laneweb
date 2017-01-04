package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class UserConfigurationTest {

    private UserConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new UserConfiguration();
    }

    @Test
    public void testLdapContextSource() {
        assertNotNull(this.configuration.ldapContextSource());
    }

    @Test
    public void testLdapDataAccess() {
        assertNotNull(this.configuration.ldapDataAccess());
    }

    @Test
    public void testSubjectSource() {
        assertNotNull(this.configuration.subjectSource());
    }
}
