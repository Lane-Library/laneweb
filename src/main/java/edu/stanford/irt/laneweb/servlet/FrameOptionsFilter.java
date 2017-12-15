package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter to add an X-Frame-Options header with value SAMEORIGIN when a referrer is present and not .stanford.edu. This
 * filter will not apply to requests lacking a referrer, including HTTPS requests. See case 112758 for more details.
 */
@WebFilter("*.html")
public class FrameOptionsFilter extends AbstractLanewebFilter {

    private static final Pattern STANFORD_PATTERN = Pattern
            .compile("^https?:\\/\\/[a-zA-Z0-9\\-]+\\.stanford.edu\\/.*");

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        String referer = request.getHeader("referer");
        if (referer != null && !STANFORD_PATTERN.matcher(referer).matches()
                && referer.indexOf(".telemetrytv.com") == -1) {
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
        }
        chain.doFilter(request, response);
    }
}
