package edu.stanford.irt.laneweb.servlet;

import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
