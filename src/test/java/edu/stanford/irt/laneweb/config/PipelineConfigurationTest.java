package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

public class PipelineConfigurationTest {

    private BeanFactory beanFactory;

    private PipelineConfiguration configuration;

    @Before
    public void setUp() {
        this.beanFactory = mock(BeanFactory.class);
        this.configuration = new PipelineConfiguration();
    }

    @Test
    public void testCachingAggregator() {
        assertNotNull(this.configuration.cachingAggregator());
    }

    @Test
    public void testCachingPipeline() {
        assertNotNull(this.configuration.cachingPipeline(null, null));
    }

    @Test
    public void testDebugTransformer() {
        assertNotNull(this.configuration.debugTransformer());
    }

    @Test
    public void testDefaultAggregator() {
        assertNotNull(this.configuration.defaultAggregator());
    }

    @Test
    public void testExpiresCachingPipeline() {
        assertNotNull(this.configuration.expiresCachingPipeline(null, null));
    }

    @Test
    public void testFileGenerator() {
        assertNotNull(this.configuration.fileGenerator(null));
    }

    @Test
    public void testHMLGenerator() {
        assertNotNull(this.configuration.htmlGenerator(this.beanFactory));
    }

    @Test
    public void testHtml5IndentSerializer() {
        assertNotNull(this.configuration.html5IndentSerializer(null));
    }

    @Test
    public void testHtml5Serializer() {
        assertNotNull(this.configuration.html5Serializer(null));
    }

    @Test
    public void testJoostTransformer() {
        assertNotNull(this.configuration.joostTransformer(null));
    }

    @Test
    public void testNamespaceFilterTransformer() {
        assertNotNull(this.configuration.namespaceFilterTransformer());
    }

    @Test
    public void testNonCachingPipeline() {
        assertNotNull(this.configuration.nonCachingPipeline());
    }

    @Test
    public void testParameterMapGenerator() {
        assertNotNull(this.configuration.parameterMapGenerator(null));
    }

    @Test
    public void testSaxonTransformer() {
        assertNotNull(this.configuration.saxonTransformer(null));
    }

    @Test
    public void testTextNodeParserTransformer() {
        assertNotNull(this.configuration.textNodeParsingTransformer(this.beanFactory));
    }

    @Test
    public void testTextSerializer() {
        assertNotNull(this.configuration.textSerializer(null));
    }

    @Test
    public void testThrottlingPipeline() {
        assertNotNull(this.configuration.throttlingPipeline());
    }

    @Test
    public void testTransformerHandler() throws TransformerConfigurationException {
        SAXTransformerFactory factory = mock(SAXTransformerFactory.class);
        TransformerHandler handler = mock(TransformerHandler.class);
        expect(factory.newTransformerHandler()).andReturn(handler);
        replay(factory);
        assertSame(handler, this.configuration.transformerHandler(factory));
        verify(factory);
    }

    @Test
    public void testXhtmlSerializer() {
        assertNotNull(this.configuration.xhtmlSerializer(null));
    }

    @Test
    public void testXIncludeTransformer() {
        assertNotNull(this.configuration.xIncludeTransformer(this.beanFactory));
    }

    @Test
    public void testXMLByteStreamInterpreter() {
        assertNotNull(this.configuration.xmlByteStreamInterpreter());
    }

    @Test
    public void testXmlSerializer() throws TransformerConfigurationException {
        assertNotNull(this.configuration.xmlSerializer(null));
    }
}
