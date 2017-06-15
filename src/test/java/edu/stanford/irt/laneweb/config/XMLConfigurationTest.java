package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class XMLConfigurationTest {

    private XMLConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new XMLConfiguration();
    }

    @Test
    public void testDocumentBuilderFactoryBean()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
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
        assertNotNull(this.configuration.joostTransformerHandlerFactory(null));
    }

    @Test
    public void testSaxonSAXTransformerFactoryBean() {
        assertNotNull(this.configuration.saxonSAXTransformerFactoryBean());
    }

    @Test
    public void testSaxonTransformerHandlerFactory() {
        assertNotNull(this.configuration.saxonTransformerHandlerFactory(null));
    }

    @Test
    public void testSAXParserFactoryBean()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
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
    public void testXIncludePipe() throws XPathFactoryConfigurationException, SAXNotRecognizedException,
            SAXNotSupportedException, ParserConfigurationException {
        assertNotNull(this.configuration.xIncludePipe(null));
    }

    @Test
    public void testXmlSAXParser()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        assertNotNull(this.configuration.xmlSAXParser());
    }

    @Test
    public void testXPathFactoryBean() throws XPathFactoryConfigurationException {
        assertNotNull(this.configuration.xPathFactoryBean());
    }

    @Test
    public void testXPointerProcessor() throws XPathFactoryConfigurationException, SAXNotRecognizedException,
            SAXNotSupportedException, ParserConfigurationException {
        assertNotNull(this.configuration.xPointerProcessor());
    }
}
