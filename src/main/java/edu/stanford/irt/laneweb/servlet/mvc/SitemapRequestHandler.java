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
import org.apache.cocoon.environment.Environment;
import org.springframework.web.HttpRequestHandler;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.LanewebEnvironment;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public abstract class SitemapRequestHandler implements HttpRequestHandler {

    protected DataBinder dataBinder;

    protected Set<String> methodsNotAllowed = Collections.emptySet();

    protected Processor processor;

    protected ServletContext servletContext;
    
    protected String prefix = "";

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        if (this.methodsNotAllowed.contains(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        
        String sitemapURI = getSitemapURI(request);
        Map<String, Object> model = new HashMap<String, Object>();
        this.dataBinder.bind(model, request);
        model.put(Model.SITEMAP_URI, sitemapURI);
        LanewebEnvironment environment = getEnvironment();
        environment.setModel(model);
        environment.setOutputStream(response.getOutputStream());
        environment.setIsExternal(true);
        
        String mimeType = getContentType(sitemapURI);
        if (mimeType != null) {
            response.setContentType(mimeType);
        }
        
        try {
            this.processor.process(environment);
        } catch (Exception e) {
            throw new LanewebException(model.toString(), e);
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
    }

    private  String getContentType(String value) {
        String contentType = this.servletContext.getMimeType(value);
        if (contentType == null) {
            if (value.indexOf("xml") > -1 || value.indexOf("rss") > -1 || value.indexOf("classes/") > -1) {
                contentType = "text/xml";
            } else if (value.indexOf("html") > -1) {
                contentType = "text/html";
            } else if (value.indexOf("json") > -1) {
                contentType = "application/json";
            } else {
                contentType = "text/plain";
            }
        }
        return contentType;
    }
    
    protected String getSitemapURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        return requestURI.substring(basePath.length() + this.prefix.length());
    }
    
    protected abstract LanewebEnvironment getEnvironment();
}
