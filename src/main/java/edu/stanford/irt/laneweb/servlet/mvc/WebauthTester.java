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
        Enumeration<?> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            sb.append("<li>").append(name).append(" --> ").append(request.getHeader(name)).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
