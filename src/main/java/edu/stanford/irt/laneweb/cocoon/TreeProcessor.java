package edu.stanford.irt.laneweb.cocoon;

import java.io.InputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.NamespacedSAXConfigurationHandler;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.Processor;
import org.apache.cocoon.components.treeprocessor.ConcreteTreeProcessor;
import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.sitemap.impl.DefaultExecutor;
import org.xml.sax.InputSource;

public class TreeProcessor implements Processor {

    private ProcessingNode rootNode;

    private SAXParser saxParser;

    private ServiceManager serviceManager;

    private SitemapLanguage sitemapLanguage;

    private InputStream source;

    
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
        NamespacedSAXConfigurationHandler handler = new NamespacedSAXConfigurationHandler();
        InputSource inputSource = new InputSource(this.source);
        this.saxParser.parse(inputSource, handler);
        Configuration sitemapProgram = handler.getConfiguration();
        this.sitemapLanguage.setProcessor(new ConcreteTreeProcessor(null, new DefaultExecutor()));
        this.rootNode = this.sitemapLanguage.build(sitemapProgram, null);
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

    public void setSAXParser(final SAXParser saxParser) {
        this.saxParser = saxParser;
    }
    
    public void setServiceManager(final ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void setSitemapLanguage(final SitemapLanguage sitemapLanguage) {
        this.sitemapLanguage = sitemapLanguage;
    }

    public void setSource(final InputStream source) {
        this.source = source;
    }

    protected boolean process(final Environment environment, final InvokeContext context) throws Exception {
        context.service(this.serviceManager);
        context.setLastProcessor(this);
        return this.rootNode.invoke(environment, context);
    }
}
