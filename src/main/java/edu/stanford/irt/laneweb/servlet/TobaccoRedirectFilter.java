package edu.stanford.irt.laneweb.servlet;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter({ "/tobacco", "/tobacco/*" })
public class TobaccoRedirectFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.addHeader("Location", "http://tobacco.stanford.edu/");
    }
}
