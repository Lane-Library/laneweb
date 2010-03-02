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

import edu.stanford.irt.laneweb.model.Model;


public class SitemapRequestHandler implements HttpRequestHandler {

    private Context context;

    private Processor processor;

    private ServletContext servletContext;

    private Map<Pattern, String> redirectMap = Collections.emptyMap();

    private Set<String> methodsNotAllowed = Collections.emptySet();

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (methodNotAllowed(request, response)) {
            return;
        }
        String uri = request.getRequestURI();
        if (uriGetsRedirect(uri, response)) {
            return;
        }
        Map<String, Object> model = createModel();
        request.setAttribute(Model.MODEL, model);
        process(request, response);
    }
    
    protected void process(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String sitemapURI = request.getRequestURI().substring(request.getContextPath().length() + 1);
        Environment environment = new HttpEnvironment(sitemapURI, request, response, this.servletContext, this.context, null, null);
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
    
    private Map<String, Object> createModel() {
        Map<String, Object> model = new HashMap<String, Object>();
        addToModel("live-base", this.servletContext.getAttribute("laneweb.context.live-base"), model);
        addToModel("stage-base", this.servletContext.getAttribute("laneweb.context.stage-base"), model);
        addToModel("medblog-base", this.servletContext.getAttribute("laneweb.context.medblog-base"), model);
        addToModel("version", this.servletContext.getAttribute("laneweb.context.version"), model);
        model.put("content-base", (this.servletContext.getAttribute("laneweb.context.live-base").toString() + "/content/"));
        model.put("resources-base", this.servletContext.getRealPath("/"));
        model.put("default-content", model.get("content-base"));
        model.put("default-resources", model.get("resources-base"));
        return model;
    }

    protected void addToModel(String key, Object value, Map<String, Object> model) {
        if (value != null) {
            if (model.containsKey(key)) {
                throw new IllegalStateException("duplicate model key: " + key);
            }
            model.put(key, value);
        }
    }
}
