package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.model.Model;


public class ContentBaseAwareHttpRequestHandler extends ResourceHttpRequestHandler {

    private List<Resource> locations;

    /**
     * Set a {@code List} of {@code Resource} paths to use as sources
     * for serving static resources.
     */
    public void setLocations(List<Resource> locations) {
        this.locations = locations;
    }

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (path == null) {
            throw new IllegalStateException("Required request attribute '" +
                    HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
        }

        if (!StringUtils.hasText(path) || path.contains("WEB-INF") || path.contains("META-INF")) {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring invalid resource path [" + path + "]");
            }
            return null;
        }
        
        URL url = (URL) request.getAttribute(Model.CONTENT_BASE);
        
        Resource contentBase = new UrlResource(url);

        if (logger.isDebugEnabled()) {
            logger.debug("Trying relative path [" + path + "] against base location: " + contentBase);
        }
        
        try {
            Resource contentBaseResource = contentBase.createRelative(path);
            if (contentBaseResource.exists() && contentBaseResource.isReadable()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found matching resource: " + contentBaseResource);
                }
                return contentBaseResource;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Resource location : this.locations) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Trying relative path [" + path + "] against base location: " + location);
                }
                Resource resource = location.createRelative(path);
                if (resource.exists() && resource.isReadable()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found matching resource: " + resource);
                    }
                    return resource;
                }
                else if (logger.isTraceEnabled()) {
                    logger.trace("Relative resource doesn't exist or isn't readable: " + resource);
                }
            }
            catch (IOException ex) {
                logger.debug("Failed to create relative resource - trying next resource location", ex);
            }
        }
        return null;
    }
}
