package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.servlet.RequestProcessor;
import org.springframework.web.HttpRequestHandler;

import edu.stanford.irt.laneweb.model.Model;

public abstract class SitemapRequestHandler implements HttpRequestHandler {

    private Set<String> methodsNotAllowed = Collections.emptySet();
    
    private Map<Pattern, String> redirectMap = Collections.emptyMap();
    
    private ProxyLinks proxyLinks;
    
    private TemplateChooser templateChooser;
    
    public void setProxyLinks(final ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }
    
    public void setTemplateChooser(final TemplateChooser templateChooser) {
        this.templateChooser = templateChooser;
    }

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (this.methodsNotAllowed.contains(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String uri = request.getRequestURI();
        for (Entry<Pattern, String> entry : this.redirectMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(uri);
            if (matcher.matches()) {
                response.sendRedirect(matcher.replaceAll(entry.getValue()));
                return;
            }
        }
        this.proxyLinks.setupProxyLinks(request);
        this.templateChooser.setupTemplate(request);
        getRequestProcessor().service(request, response);
    }

    public void setMethodsNotAllowed(final Set<String> methodsNotAllowed) {
        if (null == methodsNotAllowed) {
            throw new IllegalArgumentException("null methodsNotAllowed");
        }
        this.methodsNotAllowed = methodsNotAllowed;
    }
    
    public void setRedirectMap(Map<String, String> redirectMap) {
        if (null == redirectMap) {
            throw new IllegalArgumentException("null redirectMap");
        }
        Map<Pattern, String> newRedirectMap = new HashMap<Pattern, String>();
        for (Entry<String, String> entry : redirectMap.entrySet()) {
            newRedirectMap.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
        this.redirectMap = newRedirectMap;
    }

    protected abstract RequestProcessor getRequestProcessor();
}
