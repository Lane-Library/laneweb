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
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.http.HttpContext;
import org.springframework.web.HttpRequestHandler;

import edu.stanford.irt.laneweb.cocoon.expression.LanewebRequest;
import edu.stanford.irt.laneweb.cocoon.expression.LanewebResponse;
import edu.stanford.irt.laneweb.cocoon.pipeline.LanewebEnvironment;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public abstract class SitemapRequestHandler implements HttpRequestHandler {

    private Context context;

    private DataBinder dataBinder;

    private Set<String> methodsNotAllowed = Collections.emptySet();

    private Processor processor;

    private ServletContext servletContext;
    
    private String prefix = "";

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        if (this.methodsNotAllowed.contains(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        Map<String, Object> model = new HashMap<String, Object>();
        this.dataBinder.bind(model, request);
        String requestURI = request.getRequestURI();
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        String sitemapURI = requestURI.substring(basePath.length() + this.prefix.length());
        LanewebEnvironment environment = getEnvironment();
        environment.setModel(model);
        environment.setHttpServletResponse(response);
        environment.setHttpServletRequest(request);
        environment.setServletContext(this.servletContext);
        
        model.put(ObjectModelHelper.REQUEST_OBJECT, new LanewebRequest(sitemapURI, request));
        model.put(ObjectModelHelper.RESPONSE_OBJECT, new LanewebResponse(response));
        
        try {
            this.processor.process(environment);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void setDataBinder(final DataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
    
    protected abstract LanewebEnvironment getEnvironment();
}
