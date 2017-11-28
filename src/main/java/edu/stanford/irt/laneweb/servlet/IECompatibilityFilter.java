package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Filter that adds the X-UA-Compatible header with value IE=edge to cause IE browsers to use standards mode in
 * rendering html
 */
@WebFilter("*.html")
public class IECompatibilityFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        String userAgent = request.getHeader("user-agent");
        if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
            response.setHeader("X-UA-Compatible", "IE=edge");
        }
        chain.doFilter(request, response);
    }
}
