package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.util.Map;

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

    private Sitemap sitemap;

    private SourceResolver sourceResolver;

    protected AbstractSitemapController(final ComponentFactory componentFactory, final DataBinder dataBinder,
            final Sitemap sitemap, final SourceResolver sourceResolver) {
        this.componentFactory = componentFactory;
        this.dataBinder = dataBinder;
        this.sitemap = sitemap;
        this.sourceResolver = sourceResolver;
    }

    public abstract void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException;

    protected void doHandleRequest(final HttpServletRequest request, final HttpServletResponse response,
            final String prefix) throws IOException {
        String method = request.getMethod();
        String sitemapURI = getSitemapURI(request, prefix);
        Map<String, Object> model = this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class);
        this.dataBinder.bind(model, request);
        model.put(Model.SITEMAP_URI, sitemapURI);
        model.put(Sitemap.class.getName(), this.sitemap);
        Pipeline pipeline = this.sitemap
                .buildPipeline(new SitemapContextImpl(model, this.componentFactory, this.sourceResolver));
        response.setContentType(pipeline.getMimeType());
        if ("GET".equals(method)) {
            // only process GET requests
            pipeline.process(response.getOutputStream());
        }
    }

    protected String getSitemapURI(final HttpServletRequest request, final String prefix) {
        return request.getServletPath().substring(prefix.length());
    }
}
