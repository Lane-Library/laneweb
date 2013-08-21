package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A filter that sends an empty 404 response.  To be used for .php requests, which will always
 * be attempts to spam or vulnerability probes.
 */
public class PHPFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.sendError(HttpURLConnection.HTTP_NOT_FOUND);
    }
}
