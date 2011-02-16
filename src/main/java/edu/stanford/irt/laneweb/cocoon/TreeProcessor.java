package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.net.URL;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.NamespacedSAXConfigurationHandler;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.Processor;
import org.apache.cocoon.components.source.util.SourceUtil;
import org.apache.cocoon.components.treeprocessor.ConcreteTreeProcessor;
import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.components.treeprocessor.TreeBuilder;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.apache.cocoon.sitemap.impl.DefaultExecutor;
import org.apache.commons.logging.Log;
import org.apache.excalibur.source.Source;
import org.apache.regexp.RE;
import org.xml.sax.SAXException;

public class TreeProcessor extends org.apache.cocoon.components.treeprocessor.TreeProcessor {
    
    private EnvironmentHelper environmentHelper;

    private ProcessingNode rootNode;

    private ServiceManager serviceManager;

    private SitemapLanguage sitemapLanguage;

    private Source source;

    @Override
    public InternalPipelineDescription buildPipeline(final Environment environment) throws Exception {
        if (this.rootNode == null) {
            initialize();
        }
        InvokeContext context = new InvokeContext(true);
        try {
            if (process(environment, context)) {
                return context.getInternalPipelineDescription(environment);
            }
            return null;
        } finally {
            context.dispose();
        }
    }

    @Override
    public void configure(final Configuration config) throws ConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public org.apache.cocoon.components.treeprocessor.TreeProcessor createChildProcessor(final String src,
            final boolean configuredCheckReload, final String prefix) throws Exception {
        String name = src.substring(0, src.indexOf('.'));
        org.apache.cocoon.components.treeprocessor.TreeProcessor processor = (org.apache.cocoon.components.treeprocessor.TreeProcessor) this.serviceManager
                .lookup(Processor.class.getName() + "/" + name);
        processor.initialize();
        return processor;
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(final String arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContext() {
        return this.environmentHelper.getContext();
    }

    @Override
    public EnvironmentHelper getEnvironmentHelper() {
        return this.environmentHelper;
    }

    @Override
    public Log getLogger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Processor getParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Processor getRootProcessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SourceResolver getSourceResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleNotification() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void initialize() throws Exception {
        if (this.rootNode != null) {
            return;
        }
        Configuration sitemapProgram = createSitemapProgram(this.source);
        this.sitemapLanguage = getTreeBuilder(sitemapProgram);
        this.sitemapLanguage.setProcessor(new ConcreteTreeProcessor(null, new DefaultExecutor()) {

            @Override
            public Processor getParent() {
                return TreeProcessor.this;
            }

            @Override
            public org.apache.cocoon.components.treeprocessor.TreeProcessor getWrappingProcessor() {
                return TreeProcessor.this;
            }
        });
        this.rootNode = this.sitemapLanguage.build(sitemapProgram, this.source.getURI());
        int pos = this.source.getURI().lastIndexOf('/');
        this.environmentHelper = new EnvironmentHelper(new URL(this.source.getURI().substring(0, pos + 1)));
    }

    @Override
    public boolean process(final Environment environment) throws Exception {
        if (this.rootNode == null) {
            initialize();
        }
        InvokeContext context = new InvokeContext();
        return process(environment, context);
    }

    @Override
    public Object removeAttribute(final String arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void service(final ServiceManager serviceManager) throws ServiceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final String arg0, final Object arg1) {
        throw new UnsupportedOperationException();
    }

    public void setSource(final Source source) {
        this.source = source;
    }

    @Override
    public void setLogger(final Log l) {
        throw new UnsupportedOperationException();
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

    private SitemapLanguage getTreeBuilder(final Configuration sitemapProgram) throws ConfigurationException {
        String ns = sitemapProgram.getNamespace();
        RE re = new RE("http://apache.org/cocoon/sitemap/(\\d\\.\\d)");
        if (!re.match(ns)) {
            throw new ConfigurationException("Unknown sitemap namespace (" + ns + ") at " + this.source.getURI());
        }
        String version = re.getParen(1);
        String result = TreeBuilder.ROLE + "/sitemap-" + version;
        try {
            return (SitemapLanguage) this.serviceManager.lookup(result);
        } catch (Exception e) {
            throw new ConfigurationException("This version of Cocoon does not handle sitemap version " + version + " at "
                    + this.source.getURI(), e);
        }
    }

    protected boolean process(final Environment environment, final InvokeContext context) throws Exception {
        if (this.rootNode == null) {
            initialize();
        }
        context.service(this.serviceManager);
        context.setLastProcessor(this);
        return this.rootNode.invoke(environment, context);
    }
}
