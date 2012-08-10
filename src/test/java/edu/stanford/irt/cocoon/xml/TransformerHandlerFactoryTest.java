package edu.stanford.irt.cocoon.xml;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.CocoonException;

public class TransformerHandlerFactoryTest {

    private SAXTransformerFactory factory;

    private TransformerHandlerFactory processor;

    private Source source;

    private Templates templates;

    private Transformer transformer;

    private TransformerHandler transformerHandler;

    private URIResolver uriResolver;

    private SourceValidity validity;

    @Before
    public void setUp() {
        this.source = createMock(Source.class);
        this.uriResolver = createMock(URIResolver.class);
        this.factory = createMock(SAXTransformerFactory.class);
        this.templates = createMock(Templates.class);
        this.transformerHandler = createMock(TransformerHandler.class);
        this.transformer = createMock(Transformer.class);
        this.validity = createMock(SourceValidity.class);
        this.factory.setErrorListener(null);
        replay(this.factory);
        this.processor = new TransformerHandlerFactory(this.factory, this.uriResolver, null);
        reset(this.factory);
    }

    // TODO: test store now that not injected . . .
    // @Test
    // public void testGetTransformerHandlerSourceValid() throws
    // TransformerConfigurationException {
    // expect(this.source.getURI()).andReturn(null);
    // expect(this.source.getValidity()).andReturn(this.validity);
    // expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
    // expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
    // expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
    // this.transformer.setErrorListener(isA(ErrorListener.class));
    // this.transformer.setURIResolver(this.uriResolver);
    // replay(this.uriResolver, this.source, this.factory, this.templates,
    // this.transformerHandler, this.transformer,
    // this.validity);
    // this.processor.getTransformerHandler(this.source);
    // verify(this.uriResolver, this.source, this.factory, this.templates,
    // this.transformerHandler, this.transformer,
    // this.validity);
    // }
    @Test
    public void testGetTransformerHandlerAndValiditySourceValidNotInStore() throws TransformerConfigurationException, IOException {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.factory.newTemplates(isA(javax.xml.transform.Source.class))).andReturn(this.templates);
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener(null);
        this.transformer.setURIResolver(this.uriResolver);
        replay(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
        this.processor.getTransformerHandler(this.source);
        verify(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
    }

    @Test
    public void testGetTransformerHandlerSourceNotValid() throws IOException, TransformerConfigurationException {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(null);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.factory.newTemplates(isA(javax.xml.transform.Source.class))).andReturn(this.templates);
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
        expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
        this.transformer.setErrorListener(null);
        this.transformer.setURIResolver(this.uriResolver);
        replay(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
        this.processor.getTransformerHandler(this.source);
        verify(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
    }

    // @Test
    // public void
    // testGetTransformerHandlerAndValiditySourceValidityUnknownValid() throws
    // TransformerConfigurationException {
    // expect(this.source.getURI()).andReturn(null);
    // expect(this.source.getValidity()).andReturn(this.validity);
    // expect(this.validity.isValid()).andReturn(SourceValidity.UNKNOWN);
    // expect(this.validity.isValid(this.validity)).andReturn(SourceValidity.VALID);
    // expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
    // expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
    // this.transformer.setErrorListener(isA(ErrorListener.class));
    // this.transformer.setURIResolver(this.uriResolver);
    // replay(this.uriResolver, this.source, this.factory, this.templates,
    // this.transformerHandler, this.transformer,
    // this.validity);
    // this.processor.getTransformerHandler(this.source);
    // verify(this.uriResolver, this.source, this.factory, this.templates,
    // this.transformerHandler, this.transformer,
    // this.validity);
    // }
    //
    // @Test
    // public void
    // testGetTransformerHandlerAndValiditySourceValidityUnknownInvalid() throws
    // TransformerConfigurationException, IOException {
    // expect(this.source.getURI()).andReturn(null);
    // expect(this.source.getValidity()).andReturn(this.validity);
    // expect(this.validity.isValid()).andReturn(SourceValidity.UNKNOWN);
    // expect(this.validity.isValid(this.validity)).andReturn(SourceValidity.INVALID);
    // expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
    // expect(this.factory.newTemplates(isA(javax.xml.transform.Source.class))).andReturn(this.templates);
    // expect(this.source.getValidity()).andReturn(this.validity);
    // expect(this.factory.newTransformerHandler(this.templates)).andReturn(this.transformerHandler);
    // expect(this.transformerHandler.getTransformer()).andReturn(this.transformer);
    // this.transformer.setErrorListener(isA(ErrorListener.class));
    // this.transformer.setURIResolver(this.uriResolver);
    // replay(this.uriResolver, this.source, this.factory, this.templates,
    // this.transformerHandler, this.transformer,
    // this.validity);
    // this.processor.getTransformerHandler(this.source);
    // verify(this.uriResolver, this.source, this.factory, this.templates,
    // this.transformerHandler, this.transformer,
    // this.validity);
    // }
    @Test
    public void testGetTransformerHandlerThrowIOException() throws TransformerConfigurationException, IOException {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.source.getInputStream()).andThrow(new IOException());
        replay(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
        try {
            this.processor.getTransformerHandler(this.source);
        } catch (CocoonException e) {
        }
        verify(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
    }

    @Test
    public void testGetTransformerHandlerThrowTransformerException() throws TransformerConfigurationException, IOException {
        expect(this.source.getURI()).andReturn(null);
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.source.getInputStream()).andReturn(getClass().getResourceAsStream("test.xsl"));
        expect(this.factory.newTemplates(isA(javax.xml.transform.Source.class))).andThrow(new TransformerConfigurationException());
        replay(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
        try {
            this.processor.getTransformerHandler(this.source);
        } catch (CocoonException e) {
        }
        verify(this.uriResolver, this.source, this.factory, this.templates, this.transformerHandler, this.transformer,
                this.validity);
    }
}
