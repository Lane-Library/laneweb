package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a Filter that responds with a 403 response if there is a show parameter present. It was created to stop
 * robots for making multiple requests for expense resources with different show parameters.
 */
public class ShowParameterFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        if (request.getParameter("show") == null) {
            chain.doFilter(request, response);
        } else {
            response.sendError(HttpURLConnection.HTTP_FORBIDDEN, "show parameter not accepted");
        }
    }
}
