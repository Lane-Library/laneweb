package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;


import org.springframework.web.HttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A request handler that is designed to catch any requests that aren't handled by others, It redirects to
 * request.getRequestURI()/index.html, mimicking the way we had the last match in cocoon do a redirect.
 */
public class DefaultRequestHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String location = uri + "/index.html";
        location = queryString == null ? location : location + '?' + queryString;
        response.sendRedirect(location);
    }
}
