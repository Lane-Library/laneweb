package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.pipeline.impl.PipelineComponentInfo;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.components.treeprocessor.ProcessingNodeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;


public class SitemapLanguageTest {
    
    private Configuration configuration;
    private WebApplicationContext context;
    private ProcessingNode node;
    private ProcessingNodeBuilder nodeBuilder;
    private PipelineComponentInfo piplineInfo;
    private ServiceManager serviceManager;
    private SitemapLanguage sitemapLanguage;

    @Before
    public void setUp() throws Exception {
        this.serviceManager = createMock(ServiceManager.class);
        this.piplineInfo = createMock(PipelineComponentInfo.class);
        this.sitemapLanguage = new SitemapLanguage(this.serviceManager, this.piplineInfo);
        this.context = createMock(WebApplicationContext.class);
        this.sitemapLanguage.setApplicationContext(this.context);
        this.configuration = createMock(Configuration.class);
        this.nodeBuilder = createMock(ProcessingNodeBuilder.class);
        this.node = createMock(ProcessingNode.class);
    }

    @Test
    public void testBuildConfigurationString() throws Exception {
        expect(this.configuration.getName()).andReturn("name");
        expect(this.context.getBean("name")).andReturn(this.nodeBuilder);
        this.nodeBuilder.setBuilder(this.sitemapLanguage);
        expect(this.nodeBuilder.buildNode(this.configuration)).andReturn(this.node);
        replay(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
        assertEquals(this.node, this.sitemapLanguage.build(this.configuration, null));
        verify(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
    }

    @Test
    public void testCreateNodeBuilderConfiguration() throws Exception {
        expect(this.configuration.getName()).andReturn("name");
        expect(this.context.getBean("name")).andReturn(this.nodeBuilder);
        this.nodeBuilder.setBuilder(this.sitemapLanguage);
        replay(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
        assertEquals(this.nodeBuilder, this.sitemapLanguage.createNodeBuilder(this.configuration));
        verify(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
    }

    @Test
    public void testGetHintsForStatementStringStringConfiguration() throws Exception {
        assertEquals(Collections.emptyMap(), this.sitemapLanguage.getHintsForStatement(null, null, null));
    }

    @Test
    public void testGetRegisteredNodeString() {
        try {
            this.sitemapLanguage.getRegisteredNode("name");
            fail();
        } catch (UnsupportedOperationException e) {}
    }

    @Test
    public void testGetTypeForStatementConfigurationString() {
        expect(this.configuration.getAttribute("type", null)).andReturn("type");
        replay(this.configuration);
        assertEquals("type", this.sitemapLanguage.getTypeForStatement(this.configuration, null));
        verify(this.configuration);
    }

    @Test
    public void testGetViewsForStatementStringStringConfiguration() {
        assertEquals(Collections.emptySet(), this.sitemapLanguage.getViewsForStatement(null, null, null));
    }

    @Test
    public void testGetWebApplicationContext() {
        assertEquals(this.context, this.sitemapLanguage.getWebApplicationContext());
    }

    @Test
    public void testRegisterNodeStringProcessingNode() {
        try {
            this.sitemapLanguage.registerNode(null, null);
            fail();
        } catch (UnsupportedOperationException e) {}
    }

    @Test
    public void testResolveString() {
        replay(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
        verify(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
    }

    @Test
    public void testSetupNodeProcessingNodeConfiguration() {
        replay(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
        verify(this.serviceManager, this.piplineInfo, this.configuration, this.context, this.nodeBuilder, this.node);
    }
}
