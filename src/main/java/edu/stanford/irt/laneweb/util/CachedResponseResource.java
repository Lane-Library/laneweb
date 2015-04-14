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

    @Override
    public long contentLength() throws IOException {
        return this.cachedResponse.getBytes().length;
    }

    @Override
    public Resource createRelative(final String relativePath) throws IOException {
        return this.resource.createRelative(relativePath);
    }

    @Override
    public boolean exists() {
        return this.resource.exists();
    }

    @Override
    public String getDescription() {
        return this.resource.getDescription();
    }

    @Override
    public File getFile() throws IOException {
        return this.resource.getFile();
    }

    @Override
    public String getFilename() {
        return this.resource.getFilename();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.cachedResponse.getBytes());
    }

    @Override
    public URI getURI() throws IOException {
        return this.resource.getURI();
    }

    @Override
    public URL getURL() throws IOException {
        return this.resource.getURL();
    }

    @Override
    public boolean isOpen() {
        return this.resource.isOpen();
    }

    @Override
    public boolean isReadable() {
        return this.resource.isReadable();
    }

    @Override
    public long lastModified() throws IOException {
        return this.resource.lastModified();
    }
}
