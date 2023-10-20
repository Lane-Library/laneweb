package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.mock;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class FolioUserConfigurationTest {

    private FolioUserConfiguration configuration;

    private RESTService restService;

    private URI uri;

    @Before
    public void setUp() throws Exception {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.configuration = new FolioUserConfiguration();
    }

    @Test
    public void testUserService() {
        assertNotNull(this.configuration.userService(this.uri, this.restService));
    }
}
