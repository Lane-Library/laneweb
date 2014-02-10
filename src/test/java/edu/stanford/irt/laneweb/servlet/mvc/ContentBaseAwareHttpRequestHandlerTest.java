package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.HandlerMapping;

public class ContentBaseAwareHttpRequestHandlerTest {

    private ContentBaseAwareHttpRequestHandler handler;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.handler = new ContentBaseAwareHttpRequestHandler(getClass().getResource(
                "/edu/stanford/irt/laneweb/servlet/mvc"));
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testGetResourceHttpServletRequest() {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn(
                "ContentBaseAwareHttpRequestHandlerTest.class");
        replay(this.request);
        this.handler.getResource(this.request);
        verify(this.request);
    }
}
