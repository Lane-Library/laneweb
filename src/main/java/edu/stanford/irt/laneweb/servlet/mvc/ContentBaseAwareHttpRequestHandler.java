package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;
import edu.stanford.irt.laneweb.model.Model;

public class ContentBaseAwareHttpRequestHandler extends ResourceHttpRequestHandler {

    @Override
    protected Resource getResource(final HttpServletRequest request) {
        Resource resource = null;
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (path == null) {
            throw new IllegalStateException("Required request attribute '" + HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE
                    + "' is not set");
        }
        URL url = (URL) request.getAttribute(Model.CONTENT_BASE);
        Resource contentBase;
        try {
            contentBase = new UrlResource(url.toString() + "/");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        try {
            resource = contentBase.createRelative(path);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        resource = super.getResource(request);
        if (resource == null) {
            throw new ResourceNotFoundException(path + " not found");
        }
        return resource;
    }
}
