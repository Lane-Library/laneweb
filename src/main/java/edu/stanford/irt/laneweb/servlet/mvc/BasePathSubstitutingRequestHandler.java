package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import edu.stanford.irt.laneweb.model.Model;

public class BasePathSubstitutingRequestHandler extends ResourceHttpRequestHandler {

    private static final String SUBSTITUTE = "/./.";

    private static final Pattern PATTERN = Pattern.compile(SUBSTITUTE, Pattern.LITERAL);

    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        checkAndPrepare(request, response, true);
        // check whether a matching resource exists
        Resource resource = getResource(request);
        if (resource == null) {
            this.logger.debug("No matching resource found - returning 404");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // check the resource's media type
        MediaType mediaType = getMediaType(resource);
        if (mediaType != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Determined media type [" + mediaType + "] for " + resource);
            }
        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No media type found for " + resource + " - returning 404");
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // header phase
        setHeaders(response, resource, mediaType);
        if (new ServletWebRequest(request, response).checkNotModified(resource.lastModified())) {
            this.logger.debug("Resource not modified - returning 304");
            return;
        }
        // content phase
        if (METHOD_HEAD.equals(request.getMethod())) {
            this.logger.trace("HEAD request - skipping content");
            return;
        }
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        doWriteContent(response, resource, basePath);
    }

    private void doWriteContent(final HttpServletResponse response, final Resource resource, final String basePath)
            throws IOException {
        InputStream input = resource.getInputStream();
        PrintWriter writer = response.getWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(SUBSTITUTE) > -1) {
                    line = PATTERN.matcher(line).replaceAll(basePath);
                }
                writer.println(line);
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    protected void setHeaders(final HttpServletResponse response, final Resource resource, final MediaType mediaType)
            throws IOException {
        response.setContentType(mediaType.toString());
    }
}
