package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A servlet that sends a simple 404 response. Protects against serving .git content from the laneweb-content
 * repository. On-prem apache rules are in place and .git/devOps directories are not copied to the laneweb-content
 * bucket in GCP. This servlet an extra precaution. LANEWEB-11508
 */
@WebServlet(urlPatterns = { "/.git/*", "/.gitignore", "/.gitlab-ci.yml", "/devOps/*"  })
public class NotFoundServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        response.sendError(HttpURLConnection.HTTP_NOT_FOUND);
    }
}
