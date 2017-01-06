package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

import edu.stanford.irt.cocoon.sitemap.Sitemap;

public class SourceConfigurationTest {

    private BeanFactory beanFactory;

    private SourceConfiguration configuration;

    @Before
    public void setUp() {
        this.beanFactory = createMock(BeanFactory.class);
        this.configuration = new SourceConfiguration(this.beanFactory);
    }

    @Test
    public void testSourceResolver() {
        expect(this.beanFactory.getBean(isA(String.class), eq(Sitemap.class))).andReturn(null).times(7);
        replay(this.beanFactory);
        assertNotNull(this.configuration.sourceResolver());
        verify(this.beanFactory);
    }
}
