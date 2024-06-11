package edu.stanford.irt.laneweb.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({ "/tobacco", "/tobacco/*" })
public class TobaccoRedirectFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.addHeader("Location", "http://tobacco.stanford.edu/");
    }
}
