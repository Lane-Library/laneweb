package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RobotsDotTextServlet extends HttpServlet {
    
    private byte[] production =
        ("User-agent: *\nCrawl-delay: 7\nDisallow: /search.html\nDisallow: /secure/\n" + 
        "Disallow: /stage/\nDisallow: /online/\nDisallow: /bassett/\n" +
        "Disallow: /services/\nDisallow: /portals/history/").getBytes();
    
    private byte[] nonproduction = "User-agent: *\nDisallow: /".getBytes();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hostHeader = req.getHeader("Host");
        ServletOutputStream outputStream = resp.getOutputStream();
        if ("irt-lane-stage.stanford.edu".equals(hostHeader)) {
            outputStream.write(this.production);
        } else {
            outputStream.write(this.nonproduction);
        }
        outputStream.close();
    }

    private static final long serialVersionUID = 1L;
}
