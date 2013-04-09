package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContext;
import edu.stanford.irt.cocoon.sitemap.SitemapContextImpl;
import edu.stanford.irt.cocoon.sitemap.SitemapException;
import edu.stanford.irt.cocoon.sitemap.SitemapNode;
import edu.stanford.irt.cocoon.source.SourceResolver;


public class SitemapImpl implements Sitemap {
    
    private SitemapNode rootNode;
    private ComponentFactory componentFactory;
    private SourceResolver sourceResolver;
    
    public SitemapImpl(SitemapNode rootNode, ComponentFactory componentFactory, SourceResolver sourceResolver) {
        this.rootNode = rootNode;
        this.componentFactory = componentFactory;
        this.sourceResolver = sourceResolver;
    }

    public Pipeline buildPipeline(final Map<String, Object> model) {
        SitemapContext context = new SitemapContextImpl(model, this.componentFactory, this.sourceResolver);
        if (this.rootNode.invoke(context)) {
            return context.getPipeline();
        } else {
            throw new SitemapException("unable to create a pipeline from model: " + model);
        }
    }
}
