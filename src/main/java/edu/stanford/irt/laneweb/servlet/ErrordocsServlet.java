package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that responds to Apache errordocs requests. It sends the code found in the url or 404 if none.
 */
public class ErrordocsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Pattern pattern = Pattern.compile(".*/errordocs/(\\d\\d\\d)err.html");

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.sendError(getResponseCode(request.getRequestURI()));
    }

    private int getResponseCode(final String requestURI) {
        int responseCode;
        Matcher matcher = this.pattern.matcher(requestURI);
        if (matcher.matches()) {
            responseCode = Integer.parseInt(matcher.group(1));
        } else {
            responseCode = HttpURLConnection.HTTP_NOT_FOUND;
        }
        return responseCode;
    }
}
