package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter to add an X-Frame-Options header with value SAMEORIGIN when a referrer is present and not .stanford.edu. This
 * filter will not apply to requests lacking a referrer, including HTTPS requests. See case 112758 for more details.
 */
public class FrameOptionsFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        String referer = request.getHeader("referer");
        if (referer != null && referer.indexOf(".stanford.edu") == -1 && referer.indexOf(".telemetrytv.com") == -1) {
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
        }
        chain.doFilter(request, response);
    }
}
