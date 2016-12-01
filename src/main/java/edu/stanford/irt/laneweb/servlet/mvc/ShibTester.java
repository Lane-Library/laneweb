package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.LanewebException;

@Controller
public class ShibTester {

    // shibboleth attributes passed to tomcat : requires AJP_ prefix in shibboleth2.xml (mod_proxy_ajp)
    // or JkEnvVar directives in apache config (mod_jk) for each attribute
    private static final Collection<String> SHIB_ATTS = Arrays.asList("Shib-Identity-Provider", "displayName", "uid",
            "mail", "group");

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @RequestMapping(value = { "/secure/header-test", "/shib-secure/header-test" })
    public void testUrl(final HttpServletRequest request, final HttpServletResponse response) {
        StringBuilder result = new StringBuilder("\n\n\n<!--\n\nRequest Headers:\n\n");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            result.append(headerName).append("-->").append(request.getHeader(headerName));
            result.append('\n');
        }
        result.append("\n\n\n<!--\n\nRequest Attributes:\n\n");
        result.append("request.getRemoteUser() --> ");
        result.append(request.getRemoteUser());
        result.append('\n');
        for (String att : SHIB_ATTS) {
            result.append(att);
            result.append(" --> ");
            result.append(request.getAttribute(att));
            result.append('\n');
        }
        result.append('\n');
        Enumeration<String> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attributeName = attributes.nextElement();
            if (attributeName.indexOf("org.spring") != 0) {
                result.append(attributeName).append("-->").append(request.getAttribute(attributeName));
                result.append('\n');
            }
        }
        response.setContentType("text/plain");
        try {
            response.getOutputStream().write(result.toString().getBytes(UTF_8));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
