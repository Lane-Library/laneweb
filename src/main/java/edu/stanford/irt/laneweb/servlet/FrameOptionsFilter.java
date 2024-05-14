package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to add an X-Frame-Options header with value SAMEORIGIN when a referrer
 * is present and not .stanford.edu or another accepted host. This filter also
 * applies to requests lacking a referrer. See LANEWEB-9437, LANEWEB-10571,
 * LANEWEB-11311 for more details.
 */
@WebFilter("*.html")
public class FrameOptionsFilter extends AbstractLanewebFilter {

    private static final Pattern STANFORD_PATTERN = Pattern
            .compile("^https?:\\/\\/[a-zA-Z0-9\\-]+\\.stanford.edu\\/.*");

    private static final List<String> OK_HOSTS = new ArrayList<>(
            List.of(".telemetrytv.com", "lanestanford.libwizard.com"));

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        String referrer = request.getHeader("referer");
        boolean isValidReferrer = referrer != null && (STANFORD_PATTERN.matcher(referrer).matches()
                || (OK_HOSTS.stream().anyMatch(s -> referrer.indexOf(s) != -1)));

        if (referrer == null || !isValidReferrer) {
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
        }
        chain.doFilter(request, response);
    }
}
