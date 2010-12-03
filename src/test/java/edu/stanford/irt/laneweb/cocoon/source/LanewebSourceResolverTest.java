package edu.stanford.irt.laneweb.cocoon.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ResourceLoader;

public class LanewebSourceResolverTest {

    private ResourceLoader resourceLoader;

    private ServletContext servletContext;

    private LanewebSourceResolver sourceResolver;

    @Before
    public void setUp() throws Exception {
        this.sourceResolver = new LanewebSourceResolver();
        this.servletContext = createMock(ServletContext.class);
        expect(this.servletContext.getContextPath()).andReturn("/");
        replay(this.servletContext);
        this.sourceResolver.setServletContext(this.servletContext);
        this.resourceLoader = createMock(ResourceLoader.class);
        this.sourceResolver.setResourceLoader(this.resourceLoader);
    }

    @Test
    /*
     * test for a bug when using new URI(location) with spaces in location.
     */
    public void testSpaceInLocation() throws MalformedURLException, IOException {
        expect(this.resourceLoader.getResource("cocoon://apps/search/xml/foo bar")).andReturn(null);
        replay(this.resourceLoader);
        assertNotNull(this.sourceResolver.resolveURI("cocoon://apps/search/xml/foo bar"));
        verify(this.servletContext, this.resourceLoader);
    }
}
