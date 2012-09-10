package edu.stanford.irt.cocoon.sitemap;

import java.io.IOException;
import java.io.InputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.NamespacedSAXConfigurationHandler;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.Processor;
import org.apache.cocoon.components.treeprocessor.ConcreteTreeProcessor;
import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.components.treeprocessor.TreeBuilder;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.sitemap.impl.DefaultExecutor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.CocoonException;

public class SitemapImpl implements Processor {

    private ProcessingNode rootNode;

    private ServiceManager serviceManager;

    public SitemapImpl(final InputStream source, final SAXParser saxParser, final TreeBuilder treeBuilder,
            final ServiceManager serviceManager) throws SAXException, IOException {
        this.serviceManager = serviceManager;
        NamespacedSAXConfigurationHandler handler = new NamespacedSAXConfigurationHandler();
        InputSource inputSource = new InputSource(source);
        saxParser.parse(inputSource, handler);
        Configuration sitemapProgram = handler.getConfiguration();
        treeBuilder.setProcessor(new ConcreteTreeProcessor(null, new DefaultExecutor()));
        try {
            this.rootNode = treeBuilder.build(sitemapProgram, null);
        } catch (Exception e) {
            throw new CocoonException(e);
        }
    }

    public InternalPipelineDescription buildPipeline(final Environment environment) {
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

    public boolean process(final Environment environment) {
        InvokeContext context = new InvokeContext();
        return process(environment, context);
    }

    public Object removeAttribute(final String arg0) {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(final String arg0, final Object arg1) {
        throw new UnsupportedOperationException();
    }

    private boolean process(final Environment environment, final InvokeContext context) {
        try {
            context.service(this.serviceManager);
            context.setLastProcessor(this);
            return this.rootNode.invoke(environment, context);
        } catch (Exception e) {
            throw new CocoonException(e);
        }
    }
}
