package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Enumeration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Bots probing for vulnerabilities are sending requests with parameter value of -1%27. This filter sends them a simple
 * error message.
 */
@WebFilter({ "/search.html", "/biomed-resources/*", "/apps/*" })
public class NegativeOneAposFilter extends AbstractLanewebFilter {

    private static final String NEGATIVE_ONE_APOS = "-1'";

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        boolean hasBadParam = false;
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            if (NEGATIVE_ONE_APOS.equals(request.getParameter(names.nextElement()))) {
                hasBadParam = true;
                break;
            }
        }
        if (hasBadParam) {
            response.sendError(HttpURLConnection.HTTP_FORBIDDEN, "-1' parameter value not accepted");
        } else {
            chain.doFilter(request, response);
        }
    }
}
