package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.BasePathSubstitutingInputStream;

public class BasePathSubstitutingRequestHandler extends ResourceHttpRequestHandler {

    private static class BasePathSubstitutingResource implements Resource {

        private String basePath;

        private Resource resource;

        public BasePathSubstitutingResource(final String basePath, final Resource resource) {
            this.basePath = basePath;
            this.resource = resource;
        }

        public long contentLength() throws IOException {
            throw new IOException("can't determine length before substituting");
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

        @Override
        public InputStream getInputStream() throws IOException {
            return new BasePathSubstitutingInputStream(this.resource.getInputStream(), this.basePath);
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

    @Override
    protected Resource getResource(final HttpServletRequest request) {
        Resource resource = super.getResource(request);
        if (resource != null) {
            String basePath = (String) request.getAttribute(Model.BASE_PATH);
            return new BasePathSubstitutingResource(basePath, super.getResource(request));
        } else {
            return null;
        }
    }

    @Override
    protected void setHeaders(final HttpServletResponse response, final Resource resource, final MediaType mediaType)
            throws IOException {
        response.setContentType(mediaType.toString());
    }
}
