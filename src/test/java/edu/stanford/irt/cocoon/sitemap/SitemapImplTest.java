package edu.stanford.irt.cocoon.sitemap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.treeprocessor.ConcreteTreeProcessor;
import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.components.treeprocessor.TreeBuilder;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

import edu.stanford.irt.cocoon.CocoonException;

public class SitemapImplTest {

    private ProcessingNode processingNode;

    private ServiceManager serviceManager;

    private SitemapImpl sitemap;

    @Before
    public void setUp() throws Exception {
        SAXParser saxParser = createMock(SAXParser.class);
        this.serviceManager = createMock(ServiceManager.class);
        InputStream source = createMock(InputStream.class);
        TreeBuilder treeBuilder = createMock(TreeBuilder.class);
        this.processingNode = createMock(ProcessingNode.class);
        saxParser.parse(isA(InputSource.class), isA(ContentHandler.class));
        treeBuilder.setProcessor(isA(ConcreteTreeProcessor.class));
        expect(treeBuilder.build(null, null)).andReturn(this.processingNode);
        replay(saxParser, source, treeBuilder);
        this.sitemap = new SitemapImpl(source, saxParser, treeBuilder, this.serviceManager);
        verify(saxParser, source, treeBuilder);
    }

    @Test
    public void testBuildPipeline() throws Exception {
        Environment environment = createMock(Environment.class);
        expect(this.serviceManager.lookup(ObjectModel.class.getName())).andReturn(null);
        expect(this.processingNode.invoke(eq(environment), isA(InvokeContext.class))).andReturn(true);
        expect(environment.getURIPrefix()).andReturn(null);
        expect(environment.getURI()).andReturn(null);
        replay(this.processingNode, this.serviceManager, environment);
        assertNotNull(this.sitemap.buildPipeline(environment));
        verify(this.processingNode, this.serviceManager, environment);
    }

    @Test
    public void testBuildPipelineFalse() throws Exception {
        Environment environment = createMock(Environment.class);
        expect(this.serviceManager.lookup(ObjectModel.class.getName())).andReturn(null);
        expect(this.processingNode.invoke(eq(environment), isA(InvokeContext.class))).andReturn(false);
        replay(this.processingNode, this.serviceManager, environment);
        assertNull(this.sitemap.buildPipeline(environment));
        verify(this.processingNode, this.serviceManager, environment);
    }

    @Test
    public void testGetAttribute() {
        try {
            this.sitemap.getAttribute(null);
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetContext() {
        try {
            this.sitemap.getContext();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetParent() {
        try {
            this.sitemap.getParent();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetRootProcessor() {
        try {
            this.sitemap.getRootProcessor();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testGetSourceResolver() {
        try {
            this.sitemap.getSourceResolver();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testProcess() throws Exception {
        Environment environment = createMock(Environment.class);
        expect(this.serviceManager.lookup(ObjectModel.class.getName())).andReturn(null);
        expect(this.processingNode.invoke(eq(environment), isA(InvokeContext.class))).andReturn(true);
        replay(this.processingNode, this.serviceManager, environment);
        this.sitemap.process(environment);
        verify(this.processingNode, this.serviceManager, environment);
    }

    @Test
    public void testProcessThrowException() throws Exception {
        Environment environment = createMock(Environment.class);
        expect(this.serviceManager.lookup(ObjectModel.class.getName())).andReturn(null);
        expect(this.processingNode.invoke(eq(environment), isA(InvokeContext.class))).andThrow(new Exception());
        replay(this.processingNode, this.serviceManager, environment);
        try {
            this.sitemap.process(environment);
        } catch (CocoonException e) {
        }
        verify(this.processingNode, this.serviceManager, environment);
    }

    @Test
    public void testRemoveAttribute() {
        try {
            this.sitemap.removeAttribute(null);
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testSetAttribute() {
        try {
            this.sitemap.setAttribute(null, null);
        } catch (UnsupportedOperationException e) {
        }
    }
}
