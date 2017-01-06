package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A Controller that handlers requests with trailing slashes and redirects to request.getRequestURI()/index.html.
 */
@Controller
public class TrailingSlashController {

    @RequestMapping({ "/", "*/", "*/*/" })
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String location = uri + "/index.html";
        location = queryString == null ? location : location + '?' + queryString;
        response.sendRedirect(location);
    }
}
