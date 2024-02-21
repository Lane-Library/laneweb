package edu.stanford.irt.laneweb.servlet;

import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/main/*")
public class JavascriptTestFilter extends AbstractLanewebFilter {

    private static final Pattern MAIN = Pattern.compile("(.*)main(.*)");

    @Override
    protected void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.addHeader("Location", MAIN.matcher(request.getRequestURI()).replaceAll("$1resources$2"));
    }
}
