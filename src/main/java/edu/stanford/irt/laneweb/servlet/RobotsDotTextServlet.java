package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * sends Disallow: / if not production server. NOTE: CAB group has asked to be notified of any robots.txt changes. Send
 * description of changes to irt-change@lists.stanford.edu
 *
 * @author ceyates
 */
public class RobotsDotTextServlet extends HttpServlet {

    private static final byte[] NONPRODUCTION = "User-agent: *\nDisallow: /".getBytes(StandardCharsets.UTF_8);

    private static final byte[] PRODUCTION = ("User-agent: *\nCrawl-delay: 7\n"
        + "Disallow: /m/\n"
        + "Disallow: /search.html\nDisallow: /secure/").getBytes(StandardCharsets.UTF_8);

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        // Proxy servers add this header, may be comma separated list
        String hostHeader = req.getHeader("X-Forwarded-Host");
        if (hostHeader == null) {
            hostHeader = req.getHeader("Host");
        } else if (hostHeader.indexOf(',') > -1) {
            hostHeader = hostHeader.substring(0, hostHeader.indexOf(','));
        }
        ServletOutputStream outputStream = resp.getOutputStream();
        if ("lane.stanford.edu".equals(hostHeader)) {
            outputStream.write(PRODUCTION);
        } else {
            outputStream.write(NONPRODUCTION);
        }
        outputStream.close();
    }
}
