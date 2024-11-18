package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.xml.SAXParser;

public class SourceConfigurationTest {

    private BeanFactory beanFactory;

    private SourceConfiguration configuration;

    @Before
    public void setUp() {
        this.beanFactory = mock(BeanFactory.class);
        this.configuration = new SourceConfiguration(this.beanFactory, null, null, null, null);
    }

    @Test
    public void testSourceResolver() {
        expect(this.beanFactory.getBean(isA(String.class), eq(Sitemap.class))).andReturn(null).times(6);
        expect(this.beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/xml", SAXParser.class)).andReturn(null);
        replay(this.beanFactory);
        this.configuration.afterPropertiesSet();
        assertNotNull(this.configuration.sourceResolver());
        verify(this.beanFactory);
    }
}
