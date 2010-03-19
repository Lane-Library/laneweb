package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.xslt.XSLTProcessorException;
import org.apache.excalibur.xmlizer.XMLizer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XSLTProcessorImplTest {

    private XSLTProcessorImpl processor;

    private SourceResolver sourceResolver;

    private Store store;

    private XMLizer xmlizer;
    
    private Source source;

    @Before
    public void setUp() throws Exception {
        this.source = createMock(Source.class);
        this.store = createMock(Store.class);
//        this.xmlizer = createMock(XMLizer.class);
        this.xmlizer = new XMLizer() {

            public void toSAX(InputStream stream, String mimeType, String systemID, ContentHandler handler)
                    throws SAXException, IOException {
                XMLReader reader = XMLReaderFactory.createXMLReader();
                reader.setContentHandler(handler);
                reader.parse(new InputSource(stream));
            }};
        this.sourceResolver = createMock(SourceResolver.class);
        this.processor = new XSLTProcessorImpl(this.xmlizer, this.store, this.sourceResolver);
    }

    @Test
    public void testGetTransformerHandlerAndValiditySource() throws Exception {
        expect(this.source.getURI()).andReturn(null).times(2);
        expect(this.source.getValidity()).andReturn(null).times(3);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.source.getMimeType()).andReturn(null);
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
        } catch (UnsupportedOperationException e) {}
        verify(this.store, this.sourceResolver, this.source);
    }

    @Test
    public void testGetTransformerHandlerSource() throws Exception {
        expect(this.source.getURI()).andReturn(null).times(2);
        expect(this.source.getValidity()).andReturn(null).times(3);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.source.getMimeType()).andReturn(null);
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
        } catch (UnsupportedOperationException e) {}
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
        expect(this.source.getURI()).andReturn(null).times(3);
        expect(this.source.getValidity()).andReturn(null).times(3);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.source.getMimeType()).andReturn(null).times(2);
        this.store.remove(isA(String.class));
        replay(this.store, this.sourceResolver, this.source);
        Result result = new DOMResult();
        this.processor.initialize();
        this.processor.transform(this.source, this.source, null, result);
        verify(this.store, this.sourceResolver, this.source);
    }
}
