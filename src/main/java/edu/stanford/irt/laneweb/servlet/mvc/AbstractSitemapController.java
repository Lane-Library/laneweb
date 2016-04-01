package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContextImpl;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public abstract class AbstractSitemapController {

    private ComponentFactory componentFactory;

    private DataBinder dataBinder;

    private ServletContext servletContext;

    private Sitemap sitemap;

    private SourceResolver sourceResolver;

    public AbstractSitemapController(final ComponentFactory componentFactory, final DataBinder dataBinder,
            final ServletContext servletContext, final Sitemap sitemap, final SourceResolver sourceResolver) {
        this.componentFactory = componentFactory;
        this.dataBinder = dataBinder;
        this.servletContext = servletContext;
        this.sitemap = sitemap;
        this.sourceResolver = sourceResolver;
    }

    public abstract void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException;

    protected void doHandleRequest(final HttpServletRequest request, final HttpServletResponse response,
            final String prefix) throws IOException {
        String method = request.getMethod();
        String sitemapURI = getSitemapURI(request, prefix);
        @SuppressWarnings("unchecked")
        Map<String, Object> model = this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class);
        this.dataBinder.bind(model, request);
        model.put(Model.SITEMAP_URI, sitemapURI);
        model.put(Sitemap.class.getName(), this.sitemap);
        response.setContentType(getContentType(sitemapURI, prefix));
        Pipeline pipeline = this.sitemap
                .buildPipeline(new SitemapContextImpl(model, this.componentFactory, this.sourceResolver));
        if ("GET".equals(method)) {
            // only process GET requests
            pipeline.process(response.getOutputStream());
        }
    }

    protected String getSitemapURI(final HttpServletRequest request, final String prefix) {
        return request.getServletPath().substring(prefix.length());
    }

    private String getContentType(final String value, final String prefix) {
        String contentType = this.servletContext.getMimeType(value);
        if (contentType == null) {
            if (value.indexOf("xml") > -1) {
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
