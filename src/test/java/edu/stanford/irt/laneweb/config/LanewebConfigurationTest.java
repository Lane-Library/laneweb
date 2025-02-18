package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import jakarta.servlet.ServletContext;

public class LanewebConfigurationTest {

    private LanewebConfiguration configuration;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.configuration = new LanewebConfiguration(new URI("libguide"), new URI("libcal"), new URI("content"), "rw",
                mock(ServletContext.class), "version", "browzine-token");
    }

    @Test
    public void testComponentFactory() {
        assertNotNull(this.configuration.componentFactory(null));
    }

    @Test
    public void testJCacheManagerFactoryBean() throws URISyntaxException {
        assertNotNull(this.configuration.jCacheManagerFactoryBean());
    }

    @Test
    public void testModel() {
        Map<String, Object> model = this.configuration.model();
        assertEquals("version", model.get(Model.VERSION));
        assertEquals("content", ModelUtil.getObject(model, Model.CONTENT_BASE, URI.class).toString());
        assertEquals("libguide", ModelUtil.getObject(model, Model.LIBGUIDE_SERVICE_URI, URI.class).toString());
        assertEquals("libcal", ModelUtil.getObject(model, Model.LIBCAL_SERVICE_URI, URI.class).toString());
        assertEquals("rw", ModelUtil.getString(model, Model.BOOKMARKING));
        assertEquals("browzine-token", ModelUtil.getString(model, Model.BROWZINE_TOKEN));
    }

}
