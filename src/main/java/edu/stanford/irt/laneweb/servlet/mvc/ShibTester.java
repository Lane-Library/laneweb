package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShibTester {

    // shibboleth attributes passed to tomcat : requires AJP_ prefix in shibboleth2.xml (mod_proxy_ajp)
    // or JkEnvVar directives in apache config (mod_jk) for each attribute
    private static final Collection<String> SHIB_ATTS = Arrays.asList("Shib-Identity-Provider", "affiliation", "cn",
            "displayName", "eduPersonEntitlement", "eppn", "givenName", "group", "mail", "ou", "persistent-id",
            "postalAddress", "sn", "street", "suAffiliation", "suDisplayNameLF", "suUnivID", "targeted-id",
            "telephoneNumber", "title", "uid", "uid-alt", "unscoped-affiliation", "upn");

    @RequestMapping(value = { "/secure/header-test", "/shib-secure/header-test" })
    public void testUrl(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter result = response.getWriter();
        result.printf("\nrequest.getRemoteUser() -> %s", request.getRemoteUser());
        result.println("\n\nRequest Headers:\n");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            result.printf("%s --> %s\n", headerName, request.getHeader(headerName));
        }
        result.println("\nRequest Attributes:\n");
        result.println("Shibboleth Attributes:");
        for (String name : SHIB_ATTS) {
            Object attribute = request.getAttribute(name);
            result.printf("%s --> %s (%s)\n", name, attribute,
                    attribute == null ? "null" : attribute.getClass().getName());
        }
        Enumeration<String> names = request.getAttributeNames();
        result.println("\nOther Attributes:");
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!name.startsWith("org.springframework")) {
                Object attribute = request.getAttribute(name);
                result.printf("%s --> %s (%s)\n", name, attribute,
                        attribute == null ? "null" : attribute.getClass().getName());
            }
        }
    }
}
