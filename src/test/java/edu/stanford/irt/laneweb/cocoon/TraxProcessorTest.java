package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.store.Store;
import org.junit.Before;
import org.junit.Test;

public class TraxProcessorTest {

    private SAXTransformerFactory factory;

    private TraxProcessor processor;

    private Source source;

    private URIResolver uriResolver;

    private Store store;

    private Templates templates;

    private Transformer transformer;

    private TransformerHandler transformerHandler;

    private SourceValidity validity;

    @Before
    public void setUp() throws Exception {
        this.source = createMock(Source.class);
        this.store = createMock(Store.class);
        this.uriResolver = createMock(URIResolver.class);
        this.factory = createMock(SAXTransformerFactory.class);
        this.templates = createMock(Templates.class);
        this.transformerHandler = createMock(TransformerHandler.class);
        this.transformer = createMock(Transformer.class);
        this.validity = createMock(SourceValidity.class);
        this.factory.setErrorListener(isA(ErrorListener.class));
        replay(this.factory);
        this.processor = new TraxProcessor(this.factory, this.uriResolver, this.store);
        reset(this.factory);
    }

    @Test
    public void testGetTransformerHandlerAndValiditySourceValid() throws TransformerConfigurationException {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.store.get(isA(String.class))).andReturn(new Object[] { this.templates, this.validity });
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener(isA(ErrorListener.class));
        this.transformer.setURIResolver(this.uriResolver);
        replay(this.store, this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler,
                this.transformer, this.validity);
        this.processor.getTransformerHandler(this.source);
        verify(this.store, this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler,
                this.transformer, this.validity);
    }

    @Test
    public void testGetTransformerHandlerSourceNotValid() throws IOException, TransformerConfigurationException {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null);
        this.store.remove(isA(String.class));
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.factory.newTemplates(isA(javax.xml.transform.Source.class))).andReturn(this.templates);
        expect(this.source.getValidity()).andReturn(this.validity);
        this.store.store(isA(String.class), isA(Object[].class));
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener(isA(ErrorListener.class));
        this.transformer.setURIResolver(this.uriResolver);
        replay(this.store, this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler,
                this.transformer, this.validity);
        this.processor.getTransformerHandler(this.source);
        verify(this.store, this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler,
                this.transformer, this.validity);
    }
}
