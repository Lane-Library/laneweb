package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebauthTester {

    @RequestMapping(value = "/secure/admin/webauth")
    @ResponseBody
    public String show(final HttpServletRequest request) {
        return getWebauthAttributes(request);
    }
    
    protected String getWebauthAttributes(final HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Headers</h3>");
        sb.append("<ul>");
        Enumeration<?> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            sb.append("<li>").append(name).append(" --> ").append(request.getHeader(name)).append("</li>");
        }
        sb.append("</ul>");
        sb.append("<h3>Attributes</h3>");
        sb.append("<ul>");
        Enumeration<?> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String name = (String) attributes.nextElement();
            sb.append("<li>").append(name).append(" --> ").append(request.getAttribute(name)).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
