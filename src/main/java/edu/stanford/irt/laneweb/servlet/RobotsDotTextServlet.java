package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * sends Dissallow: / if not production server.
 * @author ceyates
 *
 */
public class RobotsDotTextServlet extends HttpServlet {
    
    private byte[] production =
        ("User-agent: *\nCrawl-delay: 7\nDisallow: /search.html\nDisallow: /secure/\n" + 
        "Disallow: /stage/\nDisallow: /online/\nDisallow: /bassett/\n" +
        "Disallow: /services/\nDisallow: /portals/history/").getBytes();
    
    private byte[] nonproduction = "User-agent: *\nDisallow: /".getBytes();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Proxy servers add this header, may be comma separated list
        String hostHeader = req.getHeader("X-Forwarded-Host");
        if (hostHeader == null) {
            hostHeader = req.getHeader("Host");
        } else if (hostHeader.indexOf(',') > -1) {
            hostHeader = hostHeader.substring(0, hostHeader.indexOf(','));
        }
        ServletOutputStream outputStream = resp.getOutputStream();
        if ("irt-lane-stage.stanford.edu".equals(hostHeader)) {
            outputStream.write(this.production);
        } else {
            outputStream.write(this.nonproduction);
        }
        outputStream.write("\n".getBytes());
        outputStream.write(hostHeader.getBytes());
        outputStream.close();
    }

    private static final long serialVersionUID = 1L;
}
