package edu.stanford.irt.laneweb.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.Resource;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class CachedResponseResource implements Resource {

    private CachedResponse cachedResponse;

    private Resource resource;

    public CachedResponseResource(final CachedResponse cachedResponse, final Resource resource) {
        this.cachedResponse = cachedResponse;
        this.resource = resource;
    }

    public long contentLength() throws IOException {
        return this.cachedResponse.getBytes().length;
    }

    public Resource createRelative(final String relativePath) throws IOException {
        return this.resource.createRelative(relativePath);
    }

    public boolean exists() {
        return this.resource.exists();
    }

    public String getDescription() {
        return this.resource.getDescription();
    }

    public File getFile() throws IOException {
        return this.resource.getFile();
    }

    public String getFilename() {
        return this.resource.getFilename();
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.cachedResponse.getBytes());
    }

    public URI getURI() throws IOException {
        return this.resource.getURI();
    }

    public URL getURL() throws IOException {
        return this.resource.getURL();
    }

    public boolean isOpen() {
        return this.resource.isOpen();
    }

    public boolean isReadable() {
        return this.resource.isReadable();
    }

    public long lastModified() throws IOException {
        return this.resource.lastModified();
    }
}
