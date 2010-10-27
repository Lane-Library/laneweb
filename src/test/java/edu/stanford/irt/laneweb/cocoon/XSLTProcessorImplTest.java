package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.xslt.XSLTProcessorException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XSLTProcessorImplTest {

    private XSLTProcessorImpl processor;

    private Source source;

    private SourceResolver sourceResolver;

    private Store store;

    private SAXParser saxParser;

    @Before
    public void setUp() throws Exception {
        this.source = createMock(Source.class);
        this.store = createMock(Store.class);
        // this.xmlizer = createMock(XMLizer.class);
        this.saxParser = new SAXParser() {

            public void parse(InputSource in, ContentHandler consumer) throws SAXException, IOException {
                XMLReader reader = XMLReaderFactory.createXMLReader();
                reader.setContentHandler(consumer);
                reader.parse(in);
            }

            public void parse(InputSource in, ContentHandler contentHandler, LexicalHandler lexicalHandler)
                    throws SAXException, IOException {
            }
        };
        this.sourceResolver = createMock(SourceResolver.class);
        this.processor = new XSLTProcessorImpl(this.saxParser, this.store, this.sourceResolver);
    }

    @Test
    public void testGetTransformerHandlerAndValiditySource() throws Exception {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null).times(3);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        this.store.remove(isA(String.class));
        replay(this.store, this.sourceResolver, this.source);
        this.processor.initialize();
        this.processor.getTransformerHandlerAndValidity(this.source);
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testGetTransformerHandlerAndValiditySourceXMLFilter() throws Exception {
        replay(this.store, this.sourceResolver, this.source);
        try {
            this.processor.getTransformerHandler(null, createMock(XMLFilter.class));
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testGetTransformerHandlerSource() throws Exception {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null).times(3);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        this.store.remove(isA(String.class));
        replay(this.store, this.sourceResolver, this.source);
        this.processor.initialize();
        this.processor.getTransformerHandler(this.source);
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testGetTransformerHandlerSourceXMLFilter() throws XSLTProcessorException {
        replay(this.store, this.sourceResolver, this.source);
        try {
            this.processor.getTransformerHandler(null, createMock(XMLFilter.class));
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    @Test
    public void testInitialize() throws Exception {
        replay(this.store, this.sourceResolver, this.source);
        this.processor.initialize();
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testResolve() throws TransformerException, MalformedURLException, IOException {
        expect(this.sourceResolver.resolveURI(null)).andReturn(this.source);
        this.sourceResolver.release(this.source);
        expect(this.source.getInputStream()).andReturn(null);
        expect(this.source.getURI()).andReturn(null);
        replay(this.store, this.sourceResolver, this.source);
        this.processor.resolve(null, null);
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testSetTransformerFactory() {
        replay(this.store, this.sourceResolver, this.source);
        this.processor.setTransformerFactory(null);
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testTransform() throws Exception {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null).times(3);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        this.store.remove(isA(String.class));
        replay(this.store, this.sourceResolver, this.source);
        Result result = new DOMResult();
        this.processor.initialize();
        this.processor.transform(this.source, this.source, null, result);
        verify(this.store, this.sourceResolver, this.source);
    }
}
