package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.HandlerMapping;

import edu.stanford.irt.laneweb.model.Model;

public class ContentBaseAwareHttpRequestHandlerTest {

    private URL contentBase;

    private ContentBaseAwareHttpRequestHandler handler;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.handler = new ContentBaseAwareHttpRequestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.contentBase = getClass().getResource("/edu/stanford/irt/laneweb/servlet/mvc");
    }

    @Test
    public void testGetResourceHttpServletRequest() {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn(
                "ContentBaseAwareHttpRequestHandlerTest.class");
        expect(this.request.getAttribute(Model.CONTENT_BASE)).andReturn(this.contentBase);
        replay(this.request);
        this.handler.getResource(this.request);
        verify(this.request);
    }
}
