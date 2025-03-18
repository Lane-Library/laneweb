package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.xml.transform.sax.SAXTransformerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class XMLConfigurationTest {

    private XMLConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new XMLConfiguration();
    }

    @Test
    public void testDocumentBuilderFactoryBean() {
        assertNotNull(this.configuration.documentBuilderFactoryBean());
    }

    @Test
    public void testErrorListener() {
        assertNotNull(this.configuration.errorListener());
    }

    @Test
    public void testHtmlFragmentSAXParser() {
        assertNotNull(this.configuration.htmlFragmentSAXParser());
    }

    @Test
    public void testHtmlSAXParser() {
        assertNotNull(this.configuration.htmlSAXParser());
    }

    @Test
    public void testJoostSAXTransformerFactoryBean() {
        assertNotNull(this.configuration.joostSAXTransformerFactoryBean());
    }

    @Test
    public void testJoostTransformerHandlerFactory() {
        assertNotNull(this.configuration.joostTransformerHandlerFactory(mock(SAXTransformerFactory.class), null, null));
    }

    @Test
    public void testSaxonSAXTransformerFactoryBean() {
        assertNotNull(this.configuration.saxonSAXTransformerFactoryBean());
    }

    @Test
    public void testSaxonTransformerHandlerFactory() {
        assertNotNull(this.configuration.saxonTransformerHandlerFactory(mock(SAXTransformerFactory.class), null, null));
    }

    @Test
    public void testSAXParserFactoryBean() {
        assertNotNull(this.configuration.saxParserFactoryBean());
    }

    @Test
    public void testTransformerFactoryBean() {
        assertNotNull(this.configuration.transformerFactoryBean());
    }

    @Test
    public void testUriResolver() {
        assertNotNull(this.configuration.uriResolver(null));
    }

    @Test
    public void testXIncludeExceptionListener() {
        assertNotNull(this.configuration.xIncludeExceptionListener());
    }

    @Test
    public void testXIncludePipe() {
        assertNotNull(this.configuration.xIncludePipe(null, null, null, null));
    }

    @Test
    public void testXmlSAXParser() {
        assertNotNull(this.configuration.xmlSAXParser(null));
    }

    @Test
    public void testXPathFactoryBean() {
        assertNotNull(this.configuration.xPathFactoryBean());
    }

    @Test
    public void testXPointerProcessor() {
        assertNotNull(this.configuration.xPointerProcessor(null, null, null, null));
    }
}
