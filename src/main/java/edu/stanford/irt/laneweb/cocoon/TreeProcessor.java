package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.NamespacedSAXConfigurationHandler;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.Processor;
import org.apache.cocoon.components.source.util.SourceUtil;
import org.apache.cocoon.components.treeprocessor.ConcreteTreeProcessor;
import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.sitemap.impl.DefaultExecutor;
import org.apache.excalibur.source.Source;
import org.xml.sax.SAXException;

public class TreeProcessor implements Processor {

    private ProcessingNode rootNode;

    private ServiceManager serviceManager;

    private SitemapLanguage sitemapLanguage;

    private Source source;

    
    public InternalPipelineDescription buildPipeline(final Environment environment) throws Exception {
        InvokeContext context = new InvokeContext(true);
        if (process(environment, context)) {
            return context.getInternalPipelineDescription(environment);
        }
        return null;
    }

    
    public Object getAttribute(final String arg0) {
        throw new UnsupportedOperationException();
    }

    
    public String getContext() {
        throw new UnsupportedOperationException();
    }

    
    public Processor getParent() {
        throw new UnsupportedOperationException();
    }

    
    public Processor getRootProcessor() {
        throw new UnsupportedOperationException();
    }

    
    public SourceResolver getSourceResolver() {
        throw new UnsupportedOperationException();
    }

    
    public void initialize() throws Exception {
        Configuration sitemapProgram = createSitemapProgram(this.source);
        this.sitemapLanguage.setProcessor(new ConcreteTreeProcessor(null, new DefaultExecutor()));
        this.rootNode = this.sitemapLanguage.build(sitemapProgram, this.source.getURI());
    }

    
    public boolean process(final Environment environment) throws Exception {
        InvokeContext context = new InvokeContext();
        return process(environment, context);
    }

    
    public Object removeAttribute(final String arg0) {
        throw new UnsupportedOperationException();
    }

    
    public void setAttribute(final String arg0, final Object arg1) {
        throw new UnsupportedOperationException();
    }

    public void setSource(final Source source) {
        this.source = source;
    }

    public void setServiceManager(final ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void setSitemapLanguage(final SitemapLanguage sitemapLanguage) {
        this.sitemapLanguage = sitemapLanguage;
    }

    private Configuration createSitemapProgram(final Source sitemapSource) throws ProcessingException, SAXException, IOException {
        NamespacedSAXConfigurationHandler handler = new NamespacedSAXConfigurationHandler();
        SourceUtil.toSAX(this.serviceManager, sitemapSource, null, handler);
        return handler.getConfiguration();
    }

    protected boolean process(final Environment environment, final InvokeContext context) throws Exception {
        context.service(this.serviceManager);
        context.setLastProcessor(this);
        return this.rootNode.invoke(environment, context);
    }
}
