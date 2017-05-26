package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class LibraryHoursConfigurationTest {

    private LibraryHoursConfiguration configuration;

    @Before
    public void setUp() {
        String privateKeyFile = getClass().getResource("test-key.p12").getFile().toString();
        this.configuration = new LibraryHoursConfiguration(privateKeyFile, "accountID", "calendarID");
    }

    @Test
    public void testCalendar() {
        assertNotNull(this.configuration.calendar());
    }

    @Test
    public void testCredential() {
        assertNotNull(this.configuration.credential());
    }

    @Test
    public void testHttpTransport() {
        assertNotNull(this.configuration.httpTransport());
    }

    @Test
    public void testJsonFactory() {
        assertNotNull(this.configuration.jsonFactory());
    }

    @Test
    public void testLibraryHoursService() {
        assertNotNull(this.configuration.libraryHoursService());
    }
}
