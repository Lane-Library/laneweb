package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A servlet that responds to Apache errordocs requests. It sends the code found in the url or 404 if none.
 */
@WebServlet("/errordocs/*")
public class ErrordocsServlet extends HttpServlet {

    private static final Pattern ERROR_PATH_PATTERN = Pattern.compile(".*/errordocs/(\\d\\d\\d)err.html");

    private static final int NOT_FOUND = 404;

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.sendError(getResponseCode(request.getRequestURI()));
    }

    private int getResponseCode(final String requestURI) {
        int responseCode;
        Matcher matcher = ERROR_PATH_PATTERN.matcher(requestURI);
        if (matcher.matches()) {
            responseCode = Integer.parseInt(matcher.group(1));
        } else {
            responseCode = NOT_FOUND;
        }
        return responseCode;
    }
}
