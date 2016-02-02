package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter redirects to https if the scheme is not https or there is not a gohttps header as set by the BigIP load
 * balancer for urls that it is doing the ssl stuff. At the moment we can't do the reverse because BigIP goes into a
 * loop if you try to redirect from https to http.
 *
 * @author ceyates
 */
public class LanewebHTTPSFilter extends AbstractLanewebFilter {

    /**
     * does the redirect if no gohttps header or scheme is not https
     */
    @Override
    protected void internalDoFilter(final HttpServletRequest req, final HttpServletResponse resp,
            final FilterChain chain) throws IOException, ServletException {
        String queryString = req.getQueryString();
        String url = queryString == null ? req.getRequestURL().toString()
                : req.getRequestURL().append('?').append(queryString).toString();
        int colonIndex = url.indexOf(':');
        if ((req.getHeader("gohttps") != null) || "https".equals(req.getScheme())) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect("https" + url.substring(colonIndex));
        }
    }
}