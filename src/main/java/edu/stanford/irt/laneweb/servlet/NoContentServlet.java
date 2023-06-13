package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A servlet that sends an empty 204 response. To be used for .php requests, which will always be attempts to spam or
 * vulnerability probes. And also used for /rss/browse and /rss/mesh requests that crawlers persist in requesting.
 */
@WebServlet({  "/rss/mesh/*", "/rss/browse/*" })
public class NoContentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        response.sendError(HttpURLConnection.HTTP_NO_CONTENT);
    }
}
