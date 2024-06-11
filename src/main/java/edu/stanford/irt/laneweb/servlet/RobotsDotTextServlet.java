package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sends Disallow: / if not production server. NOTE: CAB group has asked to be notified of any robots.txt changes. Send
 * description of changes to irt-change@lists.stanford.edu
 */
@WebServlet("/robots.txt")
public class RobotsDotTextServlet extends HttpServlet {

    private static final byte[] GCP_STAGE = "User-agent: *\nAllow: /lmldbx/\nDisallow: /"
            .getBytes(StandardCharsets.UTF_8);

    private static final Logger log = LoggerFactory.getLogger(RobotsDotTextServlet.class);

    private static final byte[] NONPRODUCTION = "User-agent: *\nDisallow: /".getBytes(StandardCharsets.UTF_8);

    private static final byte[] PRODUCTION = ("""
            User-agent: *
            Crawl-delay: 7
            Disallow: /search.html
            Disallow: /search/
            Disallow: /secure/
            Sitemap: http://lane.stanford.edu/biomed-resources/bassett/bassett-sitemap.xml
                    """).getBytes(StandardCharsets.UTF_8);

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) {
        // Proxy servers add this header, may be comma separated list
        String hostHeader = req.getHeader("X-Forwarded-Host");
        if (hostHeader == null) {
            hostHeader = req.getHeader("Host");
        } else if (hostHeader.indexOf(',') > -1) {
            hostHeader = hostHeader.substring(0, hostHeader.indexOf(','));
        }
        try {
            OutputStream outputStream = resp.getOutputStream();
            if ("lane.stanford.edu".equals(hostHeader)) {
                outputStream.write(PRODUCTION);
            } else if ("lane-prototype.stanford.edu".equals(hostHeader)) {
                outputStream.write(GCP_STAGE);
            } else {
                outputStream.write(NONPRODUCTION);
            }
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
