package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.xslt.XSLTProcessorException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;

public class XSLTProcessorImplTest {

    private SAXTransformerFactory factory;

    private XSLTProcessorImpl processor;

    private SAXParser saxParser;

    private Source source;

    private SourceResolver sourceResolver;

    private Store store;

    private Templates templates;

    private TemplatesHandler templatesHandler;

    private Transformer transformer;

    private TransformerHandler transformerHandler;
    
    private SourceValidity validity;

    @Before
    public void setUp() throws Exception {
        this.source = createMock(Source.class);
        this.store = createMock(Store.class);
        this.saxParser = createMock(SAXParser.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.factory = createMock(SAXTransformerFactory.class);
        this.templatesHandler = createMock(TemplatesHandler.class);
        this.templates = createMock(Templates.class);
        this.transformerHandler = createMock(TransformerHandler.class);
        this.transformer = createMock(Transformer.class);
        this.validity = createMock(SourceValidity.class);
        this.factory.setErrorListener(isA(ErrorListener.class));
        replay(this.factory);
        this.processor = new XSLTProcessorImpl(this.saxParser, this.store, this.sourceResolver, this.factory);
        reset(this.factory);
    }

    @Test
    public void testGetTransformerHandlerAndValiditySource() throws Exception {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(this.validity).times(1);
//        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
//        expect(this.factory.newTemplatesHandler()).andReturn(this.templatesHandler);
        expect(this.store.get(isA(String.class))).andReturn(new Object[]{this.templates, this.validity, null});
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
//        this.templatesHandler.setSystemId(null);
//        this.saxParser.parse(isA(InputSource.class), eq(this.templatesHandler));
//        expect(this.templatesHandler.getTemplates()).andReturn(this.templates);
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener((ErrorListener) isA(Object.class));
        this.transformer.setURIResolver(this.processor);
        replay(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
        this.processor.getTransformerHandlerAndValidity(this.source);
        verify(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
    }

    @Test
    public void testGetTransformerHandlerAndValiditySourceXMLFilter() throws Exception {
        replay(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
        try {
            this.processor.getTransformerHandler(null, createMock(XMLFilter.class));
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verify(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
    }

    @Test
    public void testGetTransformerHandlerSource() throws Exception {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null).times(3);
        this.store.remove(isA(String.class));
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.factory.newTemplatesHandler()).andReturn(this.templatesHandler);
        this.templatesHandler.setSystemId(null);
        this.saxParser.parse(isA(InputSource.class), eq(this.templatesHandler));
        expect(this.templatesHandler.getTemplates()).andReturn(this.templates);
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener((ErrorListener) isA(Object.class));
        this.transformer.setURIResolver(this.processor);
        replay(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
        this.processor.getTransformerHandler(this.source);
        verify(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
    }

    @Test
    public void testGetTransformerHandlerSourceXMLFilter() throws XSLTProcessorException {
        replay(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
        try {
            this.processor.getTransformerHandler(null, createMock(XMLFilter.class));
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verify(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
    }

    @Test
    public void testResolve() throws TransformerException, MalformedURLException, IOException {
        expect(this.sourceResolver.resolveURI(null)).andReturn(this.source);
        this.sourceResolver.release(this.source);
        expect(this.source.getInputStream()).andReturn(null);
        expect(this.source.getURI()).andReturn(null);
        replay(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
        this.processor.resolve(null, null);
        verify(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
    }

    @Test
    public void testTransform() throws Exception {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null).times(3);
        this.store.remove(isA(String.class));
        expect(this.factory.newTemplatesHandler()).andReturn(this.templatesHandler);
        this.templatesHandler.setSystemId(null);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl")).times(2);
        this.saxParser.parse(isA(InputSource.class), eq(this.templatesHandler));
        expect(this.templatesHandler.getTemplates()).andReturn(this.templates);
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener(isA(ErrorListener.class));
        this.transformer.setURIResolver(this.processor);
        this.transformerHandler.setResult(isA(DOMResult.class));
        this.saxParser.parse(isA(InputSource.class), eq(this.transformerHandler));
        replay(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
        Result result = new DOMResult();
        this.processor.transform(this.source, this.source, null, result);
        verify(this.store, this.sourceResolver, this.source, this.factory, this.templatesHandler, this.saxParser, this.templates,
                this.transformerHandler, this.transformer, this.validity);
    }
}
