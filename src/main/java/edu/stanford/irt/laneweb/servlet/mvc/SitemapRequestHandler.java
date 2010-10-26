package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.http.HttpContext;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.springframework.web.HttpRequestHandler;

import edu.stanford.irt.laneweb.cocoon.pipeline.LanewebEnvironment;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public abstract class SitemapRequestHandler implements HttpRequestHandler {

    private static final String[][] BASE_MAPPINGS = new String[][] {
            { "/alainb", "file:/afs/ir.stanford.edu/users/a/l/alainb/laneweb" },
            { "/ceyates", "file:/afs/ir.stanford.edu/users/c/e/ceyates/laneweb" },
            { "/olgary", "file:/afs/ir.stanford.edu/users/o/l/olgary/laneweb" },
            { "/ryanmax", "file:/afs/ir.stanford.edu/users/r/y/ryanmax/laneweb" },
            { "/ajchrist", "file:/afs/ir.stanford.edu/users/a/j/ajchrist/laneweb" },
            { "/rzwies", "file:/afs/ir.stanford.edu/users/r/z/rzwies/laneweb" } };

    private Context context;

    private Set<String> methodsNotAllowed = Collections.emptySet();

    private Processor processor;

    private ServletContext servletContext;

    protected Map<String, String> baseMappings;

    private DataBinder dataBinder;

    public SitemapRequestHandler() {
        this.baseMappings = new HashMap<String, String>(BASE_MAPPINGS.length);
        for (String[] element : BASE_MAPPINGS) {
            this.baseMappings.put(element[0], element[1]);
        }
    }

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        if (this.methodsNotAllowed.contains(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String requestURI = request.getRequestURI();
        String basePath = getBasePath(requestURI, request.getContextPath());
        String sitemapURI = requestURI.substring(basePath.length());
        Map<String, Object> model = getModel();
        this.dataBinder.bind(model, request);
        process(sitemapURI, model, request, response);
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

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
        this.context = new HttpContext(servletContext);
    }

    public void setDataBinder(DataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }
    
    private String getBasePath(final String requestURI, final String contextPath) {
        String servletPath = requestURI.substring(contextPath.length());
        if (servletPath.indexOf("/stage") ==0) {
            return contextPath + "/stage";
        }
        for (String key : this.baseMappings.keySet()) {
            if (servletPath.indexOf(key) == 0) {
                return contextPath + key;
            }
        }
        return contextPath;
    }

    protected void process(String sitemapURI, Map<String, Object> model, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException {
        Environment environment = new LanewebEnvironment(sitemapURI, model, request, response, this.servletContext,
                this.context);
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

    protected abstract Map<String, Object> getModel();
}
