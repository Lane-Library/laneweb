package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    
    private static final String[][] BASE_MAPPINGS = new String[][] {
        {"/alainb","file:/afs/ir.stanford.edu/users/a/l/alainb/laneweb"},
        {"/ceyates","file:/afs/ir.stanford.edu/users/c/e/ceyates/laneweb"},
        {"/olgary","file:/afs/ir.stanford.edu/users/o/l/olgary/laneweb"},
        {"/ryanmax","file:/afs/ir.stanford.edu/users/r/y/ryanmax/laneweb"},
        {"/ajchrist","file:/afs/ir.stanford.edu/users/a/j/ajchrist/laneweb"},
        {"/rzwies","file:/afs/ir.stanford.edu/users/r/z/rzwies/laneweb"}
    };

    private Context context;

    private Processor processor;

    private ServletContext servletContext;

    private Map<Pattern, String> redirectMap = Collections.emptyMap();

    private Set<String> methodsNotAllowed = Collections.emptySet();
    
    protected Map<String, String> baseMappings;
    
    public SitemapRequestHandler() {
        this.baseMappings = new HashMap<String, String>(BASE_MAPPINGS.length);
        for (int i = 0; i < BASE_MAPPINGS.length; i++) {
            this.baseMappings.put(BASE_MAPPINGS[i][0], BASE_MAPPINGS[i][1]);
        }
    }

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (methodNotAllowed(request, response)) {
            return;
        }
        String uri = request.getRequestURI();
        if (request.getQueryString() != null) {
            uri = uri + '?' + request.getQueryString();
        }
        if (uriGetsRedirect(uri, response)) {
            return;
        }
        uri = request.getRequestURI().substring(request.getContextPath().length());
        Map<String, Object> model = createModel(uri);
        request.setAttribute(Model.MODEL, model);
        process(request, response);
    }
    
    private String getSitemapURI(HttpServletRequest request) {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (uri.indexOf("/stage") == 0) {
            return uri.substring("/stage".length());
        }
        for (String key : this.baseMappings.keySet()) {
            if (uri.indexOf(key) == 0) {
                return uri.substring(key.length());
            }
        }
        return uri;
    }

    protected void process(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        String sitemapURI = getSitemapURI(request).substring(1);
        Environment environment = new HttpEnvironment(sitemapURI, request, response, this.servletContext, this.context, null, null);
        try {
            EnvironmentHelper.enterProcessor(this.processor, environment);
            this.processor.process(environment);
            environment.commitResponse();
        } catch (Exception e) {
            throw new ServletException(e);
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
        Map<Pattern, String> newRedirectMap = new LinkedHashMap<Pattern, String>();
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

    private Map<String, Object> createModel(String uri) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("version", this.servletContext.getAttribute("laneweb.context.version"));
        model.put("medblog-base", this.servletContext.getAttribute("laneweb.context.medblog-base"));
        model.put("default-content-base", this.servletContext.getAttribute("laneweb.context.live-base"));
        model.put("content-base", model.get("default-content-base"));
        model.put("default-resources-base", this.servletContext.getRealPath("/").toString() + "resources");
        model.put("resources-base", model.get("default-resources-base"));
        if (uri.indexOf("/stage") == 0) {
            model.put("content-base", this.servletContext.getAttribute("laneweb.context.stage-base"));
        } else {
            for (Entry<String, String> entry: this.baseMappings.entrySet()) {
                if (uri.indexOf(entry.getKey()) == 0) {
                    model.put("content-base", entry.getValue() + "/content");
                    model.put("resources-base", entry.getValue() + "/resources");
                }
            }
        }
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
