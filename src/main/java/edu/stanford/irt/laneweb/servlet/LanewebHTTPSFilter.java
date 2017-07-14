package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter redirects to https if the scheme is not https or there is not a gohttps header or a
 * x-forwarded-proto=https header
 */
public class LanewebHTTPSFilter extends AbstractLanewebFilter {

    private static final String HTTPS = "https";

    /**
     * does the redirect if scheme is not https or headers indicating that it had been
     */
    @Override
    protected void internalDoFilter(final HttpServletRequest req, final HttpServletResponse resp,
            final FilterChain chain) throws IOException, ServletException {
        String queryString = req.getQueryString();
        String url = queryString == null ? req.getRequestURL().toString()
                : req.getRequestURL().append('?').append(queryString).toString();
        int colonIndex = url.indexOf(':');
        if (HTTPS.equals(req.getScheme()) || req.getHeader("gohttps") != null
                || HTTPS.equals(req.getHeader("x-forwarded-proto"))) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect(HTTPS + url.substring(colonIndex));
        }
    }
}