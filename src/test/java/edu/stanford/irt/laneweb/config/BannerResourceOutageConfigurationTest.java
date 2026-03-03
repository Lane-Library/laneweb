package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.laneweb.resource.BannerResourceOutageService;
import edu.stanford.irt.laneweb.rest.RESTService;

public class BannerResourceOutageConfigurationTest {

    private BannerResourceOutageConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new BannerResourceOutageConfiguration();
    }

    @Test
    public void testLibanswerOutageServiceURI() throws URISyntaxException {
        URI uri = this.configuration.libanswerOutageServiceURI("https", "lanestanford.libanswers.com", -1,
                "/systems/posts");
        assertEquals(new URI("https://lanestanford.libanswers.com/systems/posts"), uri);
    }

    @Test
    public void testResourceOutageService() throws URISyntaxException {
        assertNotNull(this.configuration.resourceOutageService(new URI("/"), mock(RESTService.class)));
    }

    @Test
    public void testResourceOutageGenerator() {
        assertNotNull(this.configuration.ResourceOutageGenerator(mock(Marshaller.class), mock(SAXParser.class),
                mock(BannerResourceOutageService.class)));
    }
}
