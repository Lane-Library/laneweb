package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.mock;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class PatronRegistrationConfigurationTest {

    private PatronRegistrationConfiguration configuration;

    private RESTService restService;

    private URI uri;

    @Before
    public void setUp() throws Exception {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.configuration = new PatronRegistrationConfiguration();
    }

    @Test
    public void testUserService() {
        assertNotNull(this.configuration.userService(this.uri, this.restService));
    }
}
