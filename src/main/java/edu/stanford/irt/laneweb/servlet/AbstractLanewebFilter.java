package edu.stanford.irt.laneweb.servlet;

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
 * An abstract implementation of Filter that does the casting to
 * HttpServletRequest and HttpServletResponse so that the subclasses don't have
 * to. If the instanceof checks fail, which will never happen, the processing is
 * passed to the chain.
 */
public abstract class AbstractLanewebFilter implements Filter {

    private FilterConfig filterConfig;

    public void destroy() {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
            ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            internalDoFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(final FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    protected FilterConfig getConfiguration() {
        return this.filterConfig;
    }

    protected abstract void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException;
}
