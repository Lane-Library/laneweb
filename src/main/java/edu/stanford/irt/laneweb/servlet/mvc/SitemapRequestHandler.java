package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public abstract class SitemapRequestHandler implements HttpRequestHandler {

    private DataBinder dataBinder;

    private Set<String> methodsNotAllowed = Collections.emptySet();

    private String prefix = "";

    private Sitemap sitemap;

    private ServletContext servletContext;

    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        String method = request.getMethod();
        if (this.methodsNotAllowed.contains(method)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String sitemapURI = getSitemapURI(request);
        Map<String, Object> model = getModel();
        this.dataBinder.bind(model, request);
        model.put(Model.SITEMAP_URI, sitemapURI);
        String mimeType = getContentType(sitemapURI);
        if (mimeType != null) {
            response.setContentType(mimeType);
        }
        Pipeline pipeline = this.sitemap.buildPipeline(model);
        if ("GET".equals(method)) {
            // only process GET requests
            pipeline.process(response.getOutputStream());
        }
    }

    public void setDataBinder(final DataBinder dataBinder) {
        this.dataBinder = dataBinder;
    }

    public void setMethodsNotAllowed(final Set<String> methodsNotAllowed) {
        if (null == methodsNotAllowed) {
            throw new IllegalArgumentException("null methodsNotAllowed");
        }
        this.methodsNotAllowed = methodsNotAllowed;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public void setSitemap(final Sitemap sitemap) {
        this.sitemap = sitemap;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected abstract Map<String, Object> getModel();

    protected String getSitemapURI(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        int jsessionId = requestURI.indexOf(";jsessionid=");
        if (jsessionId > 0) {
            requestURI = requestURI.substring(0, jsessionId);
        }
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        return requestURI.substring(basePath.length() + this.prefix.length());
    }

    private String getContentType(final String value) {
        String contentType = this.servletContext.getMimeType(value);
        if (contentType == null) {
            if (value.indexOf("xml") > -1 || "/rss".equals(this.prefix) || value.indexOf("classes/") > -1) {
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
}
