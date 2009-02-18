/*
 * Created on Aug 5, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter redirects to https if the scheme is not https or there is not a
 * gohttps header as set by the BigIP load balancer for urls that it is doing
 * the ssl stuff. At the moment we can't do the reverse because BigIP goes into
 * a loop if you try to redirect from https to http.
 * 
 * @author ceyates
 */
public class LanewebHTTPSFilter implements Filter {

    public void destroy() {
    }

    /**
     * does the redirect if no gohttps header or scheme is not https
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String queryString = req.getQueryString();
        String url = queryString == null ? req.getRequestURL().toString() : req.getRequestURL().append('?').append(queryString).toString();
        int colonIndex = url.indexOf(':');
        if ((req.getHeader("gohttps") != null) || "https".equals(req.getScheme())) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect("https" + url.substring(colonIndex));
        }
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

}