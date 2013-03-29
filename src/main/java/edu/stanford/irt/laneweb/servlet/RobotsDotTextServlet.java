package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * sends Disallow: / if not production server. NOTE: CAB group has asked to be
 * notified of any robots.txt changes. Send description of changes to
 * irt-change@lists.stanford.edu
 * 
 * @author ceyates
 */
public class RobotsDotTextServlet extends HttpServlet {

    private static final String NONPRODUCTION = "User-agent: *\nDisallow: /";

    private static final String PRODUCTION = "User-agent: *\nCrawl-delay: 7\nDisallow: /bassett/\n"
            + "Disallow: /howto/\nDisallow: /m/\nDisallow: /portals/history/\n"
            + "Disallow: /search.html\nDisallow: /secure/\nDisallow: /stage/";

    private static final long serialVersionUID = 1L;

    private byte[] nonproduction;

    private byte[] production;

    public RobotsDotTextServlet() {
        try {
            this.production = PRODUCTION.getBytes("UTF-8");
            this.nonproduction = NONPRODUCTION.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        // Proxy servers add this header, may be comma separated list
        String hostHeader = req.getHeader("X-Forwarded-Host");
        if (hostHeader == null) {
            hostHeader = req.getHeader("Host");
        } else if (hostHeader.indexOf(',') > -1) {
            hostHeader = hostHeader.substring(0, hostHeader.indexOf(','));
        }
        ServletOutputStream outputStream = resp.getOutputStream();
        if ("lane.stanford.edu".equals(hostHeader)) {
            outputStream.write(this.production);
        } else {
            outputStream.write(this.nonproduction);
        }
        outputStream.close();
    }
}
