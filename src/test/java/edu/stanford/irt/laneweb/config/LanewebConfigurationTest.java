package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public class LanewebConfigurationTest {

    private LanewebConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new LanewebConfiguration();
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
        assertNotNull(this.configuration.model());
    }

    @Test
    public void testPropertySourcesPlaceholderConfigurer() {
        ResourceLoader resourceLoader = createMock(ResourceLoader.class);
        Environment environment = createMock(Environment.class);
        expect(resourceLoader.getResource("classpath:/application.properties")).andReturn(null);
        expect(resourceLoader.getResource("classpath:/config/application.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:./application.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:./config/application.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:/laneweb.properties")).andReturn(null);
        expect(resourceLoader.getResource("file:/etc/laneweb.conf")).andReturn(null);
        expect(environment.getProperty("spring.config.location"))
                .andReturn("file:/laneweb.properties,file:/etc/laneweb.conf");
        replay(environment, resourceLoader);
        assertNotNull(LanewebConfiguration.propertySourcesPlaceholderConfigurer(environment, resourceLoader));
        verify(environment, resourceLoader);
    }
}
