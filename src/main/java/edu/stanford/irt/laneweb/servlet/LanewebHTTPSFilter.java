package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter redirects to https if the scheme is not https or there is not a x-forwarded-proto=https header as set by
 * the ingress for https requests.
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
        if ("https".equals(req.getHeader("x-forwarded-proto")) || "https".equals(req.getScheme())) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect("https" + url.substring(colonIndex));
        }
    }
}