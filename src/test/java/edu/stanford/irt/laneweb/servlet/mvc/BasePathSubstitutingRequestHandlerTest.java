package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerMapping;

import edu.stanford.irt.cocoon.cache.Cache;
import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.cache.validity.NeverValid;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class BasePathSubstitutingRequestHandlerTest {

    private Cache cache;

    private File file;

    private BasePathSubstitutingRequestHandler handler;

    private MediaType mediatype;

    private HttpServletRequest request;

    private Resource resource;

    private HttpServletResponse response;

    private CachedResponse cachedResponse;

    private InputStream inputStream;

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
        this.file = createMock(File.class);
        this.cachedResponse = createMock(CachedResponse.class);
        this.inputStream = createMock(InputStream.class);
    }

    @Test
    public void testGetResource() throws IOException, URISyntaxException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("foo");
        expect(this.resource.createRelative("foo")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.resource.getURI()).andReturn(new URI("uri"));
        expect(this.cache.get(":uri")).andReturn(null);
        expect(this.resource.getInputStream()).andReturn(new ByteArrayInputStream(new byte[]{1,2,3,4}));
        expect(this.resource.getFile()).andReturn(this.file);
        expect(this.file.lastModified()).andReturn(0L);
        this.cache.store(eq(":uri"), isA(CachedResponse.class));
        replay(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
        this.handler.getResource(this.request);
        verify(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
    }

    @Test
    public void testGetResourceThrowIOException() throws IOException, URISyntaxException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("foo");
        expect(this.resource.createRelative("foo")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.resource.getURI()).andReturn(new URI("uri"));
        expect(this.cache.get(":uri")).andReturn(null);
        expect(this.resource.getInputStream()).andReturn(this.inputStream);
        expect(this.inputStream.read()).andThrow(new IOException());
        this.inputStream.close();
        replay(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
        try {
        this.handler.getResource(this.request);
        } catch (LanewebException e) {}
        verify(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
    }

    @Test
    public void testGetResourceHttpServletRequest() throws IOException, URISyntaxException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("foo");
        expect(this.resource.createRelative("foo")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.resource.getURI()).andReturn(new URI("uri"));
        expect(this.cache.get(":uri")).andReturn(null);
        expect(this.resource.getInputStream()).andReturn(new ByteArrayInputStream(new byte[0]));
        expect(this.resource.getFile()).andReturn(this.file);
        expect(this.file.lastModified()).andReturn(0L);
        this.cache.store(eq(":uri"), isA(CachedResponse.class));
        replay(this.cache, this.request, this.resource, this.file);
        this.handler.getResource(this.request);
        verify(this.cache, this.request, this.resource, this.file);
    }

    @Test
    public void testSetHeadersHttpServletResponseResourceMediaType() throws IOException {
        // can't mock toString method
        this.response.setContentType(this.mediatype.toString());
        replay(this.response, this.resource, this.mediatype);
        this.handler.setHeaders(this.response, this.resource, this.mediatype);
        verify(this.response, this.resource, this.mediatype);
    }
    
    @Test
    public void testResourceNotFound() {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("");
        expect(this.request.getRequestURI()).andReturn("uri");
        replay(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
        try {
            this.handler.getResource(this.request);
        } catch (ResourceNotFoundException e) {}
        verify(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
    }

    @Test
    public void testGetResourceCached() throws IOException, URISyntaxException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("foo");
        expect(this.resource.createRelative("foo")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.resource.getURI()).andReturn(new URI("uri"));
        expect(this.cache.get(":uri")).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidity()).andReturn(AlwaysValid.SHARED_INSTANCE);
//        expect(this.cachedResponse.getLastModified()).andReturn(0L);
//        expect(this.resource.getInputStream()).andReturn(new ByteArrayInputStream(new byte[0]));
//        this.cache.store(eq(":uri"), isA(CachedResponse.class));
        replay(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
        this.handler.getResource(this.request);
        verify(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
    }

    @Test
    public void testGetResourceCacheStale() throws IOException, URISyntaxException {
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("foo");
        expect(this.resource.createRelative("foo")).andReturn(this.resource);
        expect(this.resource.exists()).andReturn(true);
        expect(this.resource.isReadable()).andReturn(true);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.resource.getURI()).andReturn(new URI("uri"));
        expect(this.cache.get(":uri")).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidity()).andReturn(NeverValid.SHARED_INSTANCE);
//        expect(this.cachedResponse.getLastModified()).andReturn(0L);
        expect(this.resource.getFile()).andReturn(this.file);
        expect(this.file.lastModified()).andReturn(0L);
        expect(this.resource.getInputStream()).andReturn(new ByteArrayInputStream(new byte[0]));
        this.cache.store(eq(":uri"), isA(CachedResponse.class));
        replay(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
        this.handler.getResource(this.request);
        verify(this.cache, this.request, this.resource, this.response, this.mediatype, this.cachedResponse, this.inputStream, this.file);
    }
}
