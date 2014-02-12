package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class ContentBaseAwareHttpRequestHandler extends ResourceHttpRequestHandler {

    private Resource contentBase;

    public ContentBaseAwareHttpRequestHandler(final Resource contentBase) {
        this.contentBase = contentBase;
    }

    @Override
    protected Resource getResource(final HttpServletRequest request) {
        Resource resource = null;
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (path == null) {
            throw new IllegalStateException("Required request attribute '"
                    + HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
        }
        try {
            resource = this.contentBase.createRelative(path);
            if (!resource.exists() || !resource.isReadable()) {
                resource = super.getResource(request);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        if (resource == null) {
            // TODO: return null if don't need error.html page presented (eg images, etc)
            throw new ResourceNotFoundException(path + " not found");
        }
        return resource;
    }
}
