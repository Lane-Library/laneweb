package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * An abstract implementation of Filter that does the casting to HttpServletRequest and HttpServletResponse so that the
 * subclasses don't have to.
 */
public abstract class AbstractLanewebFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        internalDoFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    @Override
    public void init(final FilterConfig filterConfig) {
    }

    protected abstract void internalDoFilter(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException;
}
