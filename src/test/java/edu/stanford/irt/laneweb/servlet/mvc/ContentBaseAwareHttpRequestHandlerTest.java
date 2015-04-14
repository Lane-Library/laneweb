package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.HandlerMapping;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class ContentBaseAwareHttpRequestHandlerTest {

    private ContentBaseAwareHttpRequestHandler handler;

    private HttpServletRequest request;

    private Resource resource;

    @Before
    public void setUp() throws Exception {
        this.resource = createMock(Resource.class);
        this.handler = new ContentBaseAwareHttpRequestHandler(this.resource);
        this.handler.setLocations(Collections.singletonList(this.resource));
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testGetResource() throws IOException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("path");
        expect(this.resource.createRelative("path")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        replay(this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.request, this.resource);
    }

    @Test
    public void testGetResourceNotExists() throws IOException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("path")
        .times(2);
        expect(this.resource.createRelative("path")).andReturn(this.resource).times(2);
        expect(this.resource.exists()).andReturn(false);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.resource.getURL()).andReturn(getClass().getResource(".")).times(2);
        replay(this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.request, this.resource);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetResourceNotFound() throws IOException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("path")
        .times(2);
        expect(this.resource.createRelative("path")).andReturn(this.resource).times(2);
        expect(this.resource.exists()).andReturn(false);
        expect(this.resource.exists()).andReturn(false);
        replay(this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.request, this.resource);
    }

    @Test
    public void testGetResourceNotReadable() throws IOException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("path")
        .times(2);
        expect(this.resource.createRelative("path")).andReturn(this.resource).times(2);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(false);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.resource.getURL()).andReturn(getClass().getResource(".")).times(2);
        replay(this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.request, this.resource);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetResourceNullPath() throws IOException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn(null);
        replay(this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.request, this.resource);
    }

    @Test(expected = LanewebException.class)
    public void testGetResourceThrowsIOException() throws IOException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("path");
        expect(this.resource.createRelative("path")).andThrow(new IOException());
        replay(this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.request, this.resource);
    }
}
