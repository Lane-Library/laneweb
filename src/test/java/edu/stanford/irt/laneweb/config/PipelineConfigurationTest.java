package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

public class PipelineConfigurationTest {

    private BeanFactory beanFactory;

    private PipelineConfiguration configuration;

    @Before
    public void setUp() {
        this.beanFactory = createMock(BeanFactory.class);
        this.configuration = new PipelineConfiguration(this.beanFactory);
    }

    @Test
    public void testAggregator() {
        assertNotNull(this.configuration.aggregator());
    }

    @Test
    public void testBibStatusGenerator() {
        assertNotNull(this.configuration.bibStatusGenerator());
    }

    @Test
    public void testCachingPipeline() {
        assertNotNull(this.configuration.cachingPipeline());
    }

    @Test
    public void testClassesGenerator() {
        assertNotNull(this.configuration.classesGenerator());
    }

    @Test
    public void testDebugTransformer() {
        assertNotNull(this.configuration.debugTransformer());
    }

    @Test
    public void testEventListTransformer() {
        assertNotNull(this.configuration.eventListTransformer());
    }

    @Test
    public void testExpiresCachingPipeline() {
        assertNotNull(this.configuration.expiresCachingPipeline());
    }

    @Test
    public void testFileGenerator() {
        assertNotNull(this.configuration.fileGenerator());
    }

    @Test
    public void testHtml5IndentSerializer() {
        assertNotNull(this.configuration.html5IndentSerializer());
    }

    @Test
    public void testHtml5Serializer() {
        assertNotNull(this.configuration.html5Serializer());
    }

//    @Test
//    public void testHtmlGenerator() {
//        assertNotNull(this.configuration.htmlGenerator());
//    }

    @Test
    public void testJoostTransformer() {
        assertNotNull(this.configuration.joostTransformer());
    }

    @Test
    public void testNamespaceFilterTransformer() {
        assertNotNull(this.configuration.namespaceFilterTransformer());
    }

    @Test
    public void testNonCachedClassesGenerator() {
        assertNotNull(this.configuration.nonCachedClassesGenerator());
    }

    @Test
    public void testNonCachingPipeline() {
        assertNotNull(this.configuration.nonCachingPipeline());
    }

    @Test
    public void testParameterMapGenerator() {
        assertNotNull(this.configuration.parameterMapGenerator());
    }

    @Test
    public void testSaxonTransformer() {
        assertNotNull(this.configuration.saxonTransformer());
    }

    @Test
    public void testSeminarsGenerator() {
        assertNotNull(this.configuration.seminarsGenerator());
    }

    @Test
    public void testTextNodeParserTransformer() {
        assertNotNull(this.configuration.textNodeParsingTransformer());
    }

    @Test
    public void testTextSerializer() {
        assertNotNull(this.configuration.textSerializer());
    }

    @Test
    public void testThrottlingPipeline() {
        assertNotNull(this.configuration.throttlingPipeline());
    }

    @Test
    public void testXhtmlSerializer() {
        assertNotNull(this.configuration.xhtmlSerializer());
    }

    @Test
    public void testXIncludeTransformer() {
        assertNotNull(this.configuration.xIncludeTransformer());
    }

    @Test
    public void testXmlSerializer() {
        assertNotNull(this.configuration.xmlSerializer());
    }
}
