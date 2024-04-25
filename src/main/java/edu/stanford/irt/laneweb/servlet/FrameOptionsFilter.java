package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to add an X-Frame-Options header with value SAMEORIGIN when a referrer is present and not .stanford.edu or
 * .telemetrytv.com. This filter also applies to requests lacking a referrer. See cases 112758 and LANEWEB-10571 for
 * more details.
 */
@WebFilter("*.html")
public class FrameOptionsFilter extends AbstractLanewebFilter {

    private static final Pattern STANFORD_PATTERN = Pattern
            .compile("^https?:\\/\\/[a-zA-Z0-9\\-]+\\.stanford.edu\\/.*");

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        String referer = request.getHeader("referer");
        if (referer == null
                || (!STANFORD_PATTERN.matcher(referer).matches() && referer.indexOf(".telemetrytv.com") == -1)) {
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
        }
        chain.doFilter(request, response);
    }
}
