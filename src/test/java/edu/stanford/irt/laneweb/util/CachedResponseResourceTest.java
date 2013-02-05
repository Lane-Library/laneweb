package edu.stanford.irt.laneweb.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class CachedResponseResourceTest {

    private CachedResponse cachedResponse;

    private Resource realResource;

    private CachedResponseResource resource;

    @Before
    public void setUp() throws Exception {
        this.cachedResponse = createMock(CachedResponse.class);
        this.realResource = createMock(Resource.class);
        this.resource = new CachedResponseResource(this.cachedResponse, this.realResource);
    }

    @Test
    public void testContentLength() throws IOException {
        expect(this.cachedResponse.getBytes()).andReturn(new byte[1]);
        replay(this.cachedResponse, this.realResource);
        assertEquals(1L, this.resource.contentLength());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testCreateRelative() throws IOException {
        expect(this.realResource.createRelative("foo")).andReturn(this.realResource);
        replay(this.cachedResponse, this.realResource);
        assertEquals(this.realResource, this.resource.createRelative("foo"));
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testExists() {
        expect(this.realResource.exists()).andReturn(true);
        replay(this.cachedResponse, this.realResource);
        assertTrue(this.resource.exists());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testGetDescription() {
        expect(this.realResource.getDescription()).andReturn("description");
        replay(this.cachedResponse, this.realResource);
        assertEquals("description", this.resource.getDescription());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testGetFile() throws IOException {
        expect(this.realResource.getFile()).andReturn(null);
        replay(this.cachedResponse, this.realResource);
        assertNull(this.resource.getFile());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testGetFilename() {
        expect(this.realResource.getFilename()).andReturn("filename");
        replay(this.cachedResponse, this.realResource);
        assertEquals("filename", this.resource.getFilename());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testGetInputStream() throws IOException {
        expect(this.cachedResponse.getBytes()).andReturn(new byte[0]);
        replay(this.cachedResponse, this.realResource);
        assertEquals(-1, this.resource.getInputStream().read());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testGetURI() throws IOException {
        expect(this.realResource.getURI()).andReturn(null);
        replay(this.cachedResponse, this.realResource);
        assertNull(this.resource.getURI());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testGetURL() throws IOException {
        expect(this.realResource.getURL()).andReturn(null);
        replay(this.cachedResponse, this.realResource);
        assertNull(this.resource.getURL());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testIsOpen() {
        expect(this.realResource.isOpen()).andReturn(true);
        replay(this.cachedResponse, this.realResource);
        assertTrue(this.resource.isOpen());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testIsReadable() {
        expect(this.realResource.isReadable()).andReturn(true);
        replay(this.cachedResponse, this.realResource);
        assertTrue(this.resource.isReadable());
        verify(this.cachedResponse, this.realResource);
    }

    @Test
    public void testLastModified() throws IOException {
        expect(this.realResource.lastModified()).andReturn(1L);
        replay(this.cachedResponse, this.realResource);
        assertEquals(1L, this.resource.lastModified());
        verify(this.cachedResponse, this.realResource);
    }
}
