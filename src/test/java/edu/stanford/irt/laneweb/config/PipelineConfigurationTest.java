package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.mock;
import static org.junit.Assert.assertNotNull;

import javax.xml.transform.TransformerConfigurationException;

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
    public void testAggregator() {
        assertNotNull(this.configuration.aggregator());
    }

    @Test
    public void testCachingPipeline() {
        assertNotNull(this.configuration.cachingPipeline(null));
    }

    @Test
    public void testDebugTransformer() {
        assertNotNull(this.configuration.debugTransformer());
    }

    @Test
    public void testEventListTransformer() {
        assertNotNull(this.configuration.eventListTransformer(null, null));
    }

    @Test
    public void testExpiresCachingPipeline() {
        assertNotNull(this.configuration.expiresCachingPipeline(null));
    }

    @Test
    public void testFileGenerator() {
        assertNotNull(this.configuration.fileGenerator(null));
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
    public void testSeminarsGenerator() {
        assertNotNull(this.configuration.seminarsGenerator(this.beanFactory, null));
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
    public void testXhtmlSerializer() {
        assertNotNull(this.configuration.xhtmlSerializer(null));
    }

    @Test
    public void testXIncludeTransformer() {
        assertNotNull(this.configuration.xIncludeTransformer(this.beanFactory));
    }

    @Test
    public void testXmlSerializer() throws TransformerConfigurationException {
        assertNotNull(this.configuration.xmlSerializer(null));
    }
}
