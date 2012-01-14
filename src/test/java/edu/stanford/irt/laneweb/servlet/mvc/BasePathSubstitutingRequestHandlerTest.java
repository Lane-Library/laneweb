package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerMapping;

import edu.stanford.irt.laneweb.model.Model;

public class BasePathSubstitutingRequestHandlerTest {

    private Cache cache;

    private BasePathSubstitutingRequestHandler handler;

    private MediaType mediatype;

    private HttpServletRequest request;

    private Resource resource;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.handler = new BasePathSubstitutingRequestHandler();
        this.cache = createMock(Cache.class);
        this.resource = createMock(Resource.class);
        this.handler.setCache(this.cache);
        this.handler.setLocations(Collections.<Resource> singletonList(this.resource));
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.mediatype = createMock(MediaType.class);
    }

    @Test
    public void testGetResourceHttpServletRequest() throws IOException, URISyntaxException, ProcessingException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("foo");
        expect(this.resource.createRelative("foo")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.request.getAttribute(Model.BASE_PATH)).andReturn("");
        expect(this.resource.getURI()).andReturn(new URI("uri"));
        expect(this.resource.lastModified()).andReturn(0L);
        expect(this.cache.get(":uri")).andReturn(null);
        expect(this.resource.getInputStream()).andReturn(new ByteArrayInputStream(new byte[0]));
        this.cache.store(eq(":uri"), isA(CachedResponse.class));
        replay(this.cache, this.request, this.resource);
        this.handler.getResource(this.request);
        verify(this.cache, this.request, this.resource);
    }

    @Test
    public void testSetHeadersHttpServletResponseResourceMediaType() throws IOException {
        // can't mock toString method
        this.response.setContentType(this.mediatype.toString());
        replay(this.response, this.resource, this.mediatype);
        this.handler.setHeaders(this.response, this.resource, this.mediatype);
        verify(this.response, this.resource, this.mediatype);
    }
}
