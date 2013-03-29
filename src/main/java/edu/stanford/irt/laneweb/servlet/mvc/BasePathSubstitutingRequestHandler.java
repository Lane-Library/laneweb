package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.cocoon.cache.Cache;
import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.source.FileTimeStampValidity;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.BasePathSubstitutingInputStream;
import edu.stanford.irt.laneweb.util.CachedResponseResource;

public class BasePathSubstitutingRequestHandler extends ResourceHttpRequestHandler {

    private Cache cache;

    public void setCache(final Cache cache) {
        this.cache = cache;
    }

    @Override
    // TODO: this can probably be a controller.
    protected Resource getResource(final HttpServletRequest request) {
        Resource resource = super.getResource(request);
        if (resource != null) {
            try {
                String basePath = (String) request.getAttribute(Model.BASE_PATH);
                String cacheKey = basePath + ":" + resource.getURI();
                CachedResponse cachedResponse = this.cache.get(cacheKey);
                if (cachedResponse == null || !cachedResponse.getValidity().isValid()) {
                    InputStream input = new BasePathSubstitutingInputStream(resource.getInputStream(), basePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i;
                    try {
                        while ((i = input.read()) != -1) {
                            baos.write(i);
                        }
                    } finally {
                        input.close();
                        baos.close();
                    }
                    cachedResponse = new CachedResponse(new FileTimeStampValidity(resource.getFile()), baos.toByteArray());
                    this.cache.store(cacheKey, cachedResponse);
                }
                return new CachedResponseResource(cachedResponse, resource);
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        } else {
            throw new ResourceNotFoundException(request.getRequestURI() + " not found.");
        }
    }

    @Override
    protected void setHeaders(final HttpServletResponse response, final Resource resource, final MediaType mediaType)
            throws IOException {
        response.setContentType(mediaType.toString());
    }
}
