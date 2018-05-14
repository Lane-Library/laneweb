package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class LanewebConfigurationTest {

    private LanewebConfiguration configuration;

    @Before
    public void setUp() throws URISyntaxException {
        this.configuration = new LanewebConfiguration(new URI("classes"), new URI("content"), Boolean.TRUE,
                mock(ServletContext.class), "version");
    }

    @Test
    public void testComponentFactory() {
        assertNotNull(this.configuration.componentFactory());
    }

    @Test
    public void testJCacheManagerFactoryBean() throws URISyntaxException {
        assertNotNull(this.configuration.jCacheManagerFactoryBean());
    }

    @Test
    public void testMarshaller() {
        assertNotNull(this.configuration.marshaller());
    }

    @Test
    public void testModel() {
        Map<String, Object> model = this.configuration.model();
        assertEquals("version", model.get(Model.VERSION));
        assertSame(Boolean.TRUE, model.get(Model.DISASTER_MODE));
        assertEquals("content", ModelUtil.getObject(model, Model.CONTENT_BASE, URI.class).toString());
        assertEquals("classes", ModelUtil.getObject(model, Model.CLASSES_SERVICE_URI, URI.class).toString());
    }

    @Test
    public void testPropertySourcesPlaceholderConfigurer() {
        ResourceLoader resourceLoader = mock(ResourceLoader.class);
        Environment environment = mock(Environment.class);
        expect(resourceLoader.getResource("classpath:/application.properties")).andReturn(null);
        expect(resourceLoader.getResource("classpath:/config/application.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:./application.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:./config/application.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:/laneweb.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:/etc/laneweb.conf")).andReturn(null);
        expect(environment.getActiveProfiles()).andReturn(new String[] { "gce" });
        expect(resourceLoader.getResource("classpath:/application-gce.properties")).andReturn(null);
        expect(resourceLoader.getResource("classpath:/config/application-gce.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:./application-gce.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:./config/application-gce.properties")).andReturn(null);
        expect(environment.getProperty("spring.config.location"))
                .andReturn("file:/laneweb.properties,file:/etc/laneweb.conf");
        replay(environment, resourceLoader);
        assertNotNull(LanewebConfiguration.propertySourcesPlaceholderConfigurer(environment, resourceLoader));
        verify(environment, resourceLoader);
    }

    @Test
    public void testServiceURIResolver() {
        assertNotNull(this.configuration.serviceURIResolver());
    }
}
