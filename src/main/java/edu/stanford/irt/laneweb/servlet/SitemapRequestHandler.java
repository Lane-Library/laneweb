package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.http.HttpContext;
import org.apache.cocoon.environment.http.HttpEnvironment;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.springframework.web.HttpRequestHandler;

public class SitemapRequestHandler implements HttpRequestHandler {

    private Context context;

    private Set<String> methodsNotAllowed = Collections.emptySet();

    private Processor processor;

    private ProxyLinks proxyLinks;

    private Map<Pattern, String> redirectMap = Collections.emptyMap();

    private ServletContext servletContext;

    private TemplateChooser templateChooser;

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (methodNotAllowed(request, response)) {
            return;
        }
        String uri = request.getRequestURI();
        if (uriGetsRedirect(uri, response)) {
            return;
        }
        this.proxyLinks.setupProxyLinks(request);
        this.templateChooser.setupTemplate(request);
        Environment environment = new HttpEnvironment(uri.substring(1), request, response, this.servletContext, this.context, null, null);
        try {
            EnvironmentHelper.enterProcessor(this.processor, environment);
            this.processor.process(environment);
            environment.commitResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            EnvironmentHelper.leaveProcessor();
        }
    }

    public void setMethodsNotAllowed(final Set<String> methodsNotAllowed) {
        if (null == methodsNotAllowed) {
            throw new IllegalArgumentException("null methodsNotAllowed");
        }
        this.methodsNotAllowed = methodsNotAllowed;
    }

    public void setProcessor(final Processor processor) {
        this.processor = processor;
    }

    public void setProxyLinks(final ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    public void setRedirectMap(final Map<String, String> redirectMap) {
        if (null == redirectMap) {
            throw new IllegalArgumentException("null redirectMap");
        }
        Map<Pattern, String> newRedirectMap = new HashMap<Pattern, String>();
        for (Entry<String, String> entry : redirectMap.entrySet()) {
            newRedirectMap.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
        this.redirectMap = newRedirectMap;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
        this.context = new HttpContext(servletContext);
    }

    public void setTemplateChooser(final TemplateChooser templateChooser) {
        this.templateChooser = templateChooser;
    }

    private boolean methodNotAllowed(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (this.methodsNotAllowed.contains(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return true;
        }
        return false;
    }

    private boolean uriGetsRedirect(final String uri, final HttpServletResponse response) throws IOException {
        for (Entry<Pattern, String> entry : this.redirectMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(uri);
            if (matcher.matches()) {
                response.sendRedirect(matcher.replaceAll(entry.getValue()));
                return true;
            }
        }
        return false;
    }
}
