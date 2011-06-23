package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.excalibur.source.SourceValidity;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.BasePathSubstitutingInputStream;

public class BasePathSubstitutingRequestHandler extends ResourceHttpRequestHandler {

    private static class CachedResponseResource implements Resource {

        private CachedResponse cachedResponse;

        protected Resource resource;

        public CachedResponseResource(final CachedResponse cachedResponse, final Resource resource) {
            this.cachedResponse = cachedResponse;
            this.resource = resource;
        }

        public long contentLength() throws IOException {
            return this.cachedResponse.getResponse().length;
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
            return new ByteArrayInputStream(this.cachedResponse.getResponse());
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

    private Cache cache;
    
    public void setCache(final Cache cache) {
        this.cache = cache;
    }

    @Override
    protected Resource getResource(final HttpServletRequest request) {
        Resource resource = super.getResource(request);
        if (resource != null) {
            try {
                String basePath = (String) request.getAttribute(Model.BASE_PATH);
                String cacheKey = basePath + ":" + resource.getURI();
                long lastModified = resource.lastModified();
                CachedResponse cachedResponse = this.cache.get(cacheKey);
                if (cachedResponse == null || lastModified > cachedResponse.getLastModified()) {
                    InputStream input = new BasePathSubstitutingInputStream(resource.getInputStream(), basePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i;
                    while ((i = input.read()) != -1) {
                        baos.write(i);
                    }
                    input.close();
                    baos.close();
                    cachedResponse = new CachedResponse((SourceValidity[]) null, baos.toByteArray());
                    this.cache.store(cacheKey, cachedResponse);
                }
                return new CachedResponseResource(cachedResponse, resource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException(request.getRequestURI() + " not found.");
        }
    }

    @Override
    protected void setHeaders(final HttpServletResponse response, final Resource resource, final MediaType mediaType)
            throws IOException {
        response.setContentType(mediaType.toString());
    }
}
