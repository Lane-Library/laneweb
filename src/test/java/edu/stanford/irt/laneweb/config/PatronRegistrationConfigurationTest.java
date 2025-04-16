package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;

public class PatronRegistrationConfigurationTest {

    private PatronRegistrationConfiguration configuration;

    private BasicAuthRESTService restService;

    private URI uri;

    @BeforeEach
    public void setUp() throws Exception {
        this.uri = new URI("/");
        this.restService = mock(BasicAuthRESTService.class);
        this.configuration = new PatronRegistrationConfiguration();
    }

    @Test
    public void testUserService() {
        assertNotNull(this.configuration.userService(this.uri, this.restService));
    }
}
