package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.LanewebException;

@Controller
public class ShibTester {

    @RequestMapping(value = { "/secure/header-test", "/shib-secure/header-test" })
    public void testUrl(final HttpServletRequest request, final HttpServletResponse response) {
        StringBuilder result = new StringBuilder("\n\n\n<!--\n\nRequest Headers:\n\n");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            result.append(headerName).append("-->").append(request.getHeader(headerName));
            result.append("\n");
        }
        // these all work: require AJP_ prefix in shibboleth2.xml
        result.append("\n\n\n<!--\n\nRequest Attributes:\n\n");
        result.append("Shib-Identity-Provider --> ");
        result.append(request.getAttribute("Shib-Identity-Provider"));
        result.append("\n");
        result.append("request.getRemoteUser() --> ");
        result.append(request.getRemoteUser());
        result.append("\n");
        result.append("displayName --> ");
        result.append(request.getAttribute("displayName"));
        result.append("\n");
        result.append("uid --> ");
        result.append(request.getAttribute("uid"));
        result.append("\n");
        result.append("mail --> ");
        result.append(request.getAttribute("mail"));
        result.append("\n");
        result.append("group --> ");
        result.append(request.getAttribute("group"));
        result.append("\n");
        result.append("\n");
        Enumeration<String> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attributeName = attributes.nextElement();
            if (attributeName.indexOf("org.spring") != 0) {
                result.append(attributeName).append("-->").append(request.getAttribute(attributeName));
                result.append("\n");
            }
        }
        response.setContentType("text/plain");
        try {
            response.getOutputStream().write(result.toString().getBytes("UTF-8"));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
