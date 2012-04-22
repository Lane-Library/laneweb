package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class LanewebTraxTransformerTest {
    
    private LanewebTraxTransformer transformer;
    
    private char[] chars;

    private XMLConsumer xmlConsumer;

    private Locator locator;

    private Attributes attributes;

    private TraxProcessor traxProcessor;

    private SourceResolver sourceResolver;

    private Map<String, Object> model;

    private Parameters parameters;

    private Source source;

    private TransformerHandler handler;

    private Transformer traxTransformer;

    private SourceValidity validity;

    @Before
    public void setUp() throws Exception {
        this.traxProcessor = createMock(TraxProcessor.class);
        this.chars = new char[0];
        this.transformer = new LanewebTraxTransformer(this.traxProcessor);
        this.sourceResolver = createMock(SourceResolver.class);
        this.source = createMock(Source.class);
        this.parameters = createMock(Parameters.class);
        this.handler = createMock(TransformerHandler.class);
        this.model = Collections.emptyMap();
        this.traxTransformer = createMock(Transformer.class);
        this.validity = createMock(SourceValidity.class);
        expect(this.sourceResolver.resolveURI("src")).andReturn(this.source);
        expect(this.parameters.getParameter("cache-key", null)).andReturn("cache-key");
        expect(this.source.getValidity()).andReturn(this.validity);
        expect(this.traxProcessor.getTransformerHandler(this.source)).andReturn(this.handler);
        expect(this.handler.getTransformer()).andReturn(this.traxTransformer);
        expect(this.parameters.getNames()).andReturn(new String[0]);
        this.handler.setResult(isA(SAXResult.class));
        this.handler.startDocument();
        replay(this.sourceResolver, this.source, this.parameters, this.traxProcessor, this.handler, this.traxTransformer);
        this.transformer.setup(this.sourceResolver, this.model , "src", this.parameters);
        this.transformer.setConsumer(this.xmlConsumer);
        this.transformer.startDocument();
        verify(this.sourceResolver, this.source, this.parameters, this.traxProcessor, this.handler, this.traxTransformer);
        reset(this.handler);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.handler.characters(this.chars, 0, 0);
        replay(this.handler);
        this.transformer.characters(this.chars, 0, 0);
        verify(this.handler);
    }

    @Test
    public void testComment() throws SAXException {
        this.handler.comment(this.chars, 0, 0);
        replay(this.handler);
        this.transformer.comment(this.chars, 0, 0);
        verify(this.handler);
    }

    @Test
    public void testEndCDATA() throws SAXException {
        this.handler.endCDATA();
        replay(this.handler);
        this.transformer.endCDATA();
        verify(this.handler);
    }

    @Test
    public void testEndDocument() throws SAXException {
        this.handler.endDocument();
        replay(this.handler);
        this.transformer.endDocument();
        verify(this.handler);
    }

    @Test
    public void testEndDTD() throws SAXException {
        this.handler.endDTD();
        replay(this.handler);
        this.transformer.endDTD();
        verify(this.handler);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.handler.endElement("uri", "localName", "qName");
        replay(this.handler);
        this.transformer.endElement("uri", "localName", "qName");
        verify(this.handler);
    }

    @Test
    public void testEndEntity() throws SAXException {
        this.handler.endEntity("name");
        replay(this.handler);
        this.transformer.endEntity("name");
        verify(this.handler);
    }

    @Test
    public void testEndPrefixMapping() throws SAXException {
        this.handler.endPrefixMapping("prefix");
        replay(this.handler);
        this.transformer.endPrefixMapping("prefix");
        verify(this.handler);
    }

    @Test
    public void testGetKey() {
        reset(this.source);
      expect(this.source.getURI()).andReturn("uri");
      replay(this.source);
        this.transformer.getKey();
        verify(this.source);
    }

    @Test
    public void testGetValidity() {
        this.transformer.getValidity();
    }

    @Test
    public void testIgnorableWhitespace() throws SAXException {
        this.handler.ignorableWhitespace(this.chars, 0, 0);
        replay(this.handler);
        this.transformer.ignorableWhitespace(this.chars, 0, 0);
        verify(this.handler);
    }

    @Test
    public void testNotationDecl() throws SAXException {
        this.handler.notationDecl("name", "publicId", "systemId");
        replay(this.handler);
        this.transformer.notationDecl("name", "publicId", "systemId");
        verify(this.handler);
    }

    @Test
    public void testProcessingInstruction() throws SAXException {
        this.handler.processingInstruction("target", "data");
        replay(this.handler);
        this.transformer.processingInstruction("target", "data");
        verify(this.handler);
    }

    @Test
    public void testSetConsumer() {
        this.transformer.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testSetDocumentLocator() {
        this.transformer.setDocumentLocator(this.locator);
    }

    @Test
    public void testSkippedEntity() throws SAXException {
        this.handler.skippedEntity("name");
        replay(this.handler);
        this.transformer.skippedEntity("name");
        verify(this.handler);
    }

    @Test
    public void testStartCDATA() throws SAXException {
        this.handler.startCDATA();
        replay(this.handler);
        this.transformer.startCDATA();
        verify(this.handler);
    }

    @Test
    public void testStartDTD() throws SAXException {
        this.handler.startDTD("name", "publicId", "systemId");
        replay(this.handler);
        this.transformer.startDTD("name", "publicId", "systemId");
        verify(this.handler);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.handler.startElement("uri", "localName", "qName", this.attributes);
        replay(this.handler);
        this.transformer.startElement("uri", "localName", "qName", this.attributes);
        verify(this.handler);
    }

    @Test
    public void testStartEntity() throws SAXException {
        this.handler.startEntity("name");
        replay(this.handler);
        this.transformer.startEntity("name");
        verify(this.handler);
    }

    @Test
    public void testStartPrefixMapping() throws SAXException {
        this.handler.startPrefixMapping("prefix", "uri");
        replay(this.handler);
        this.transformer.startPrefixMapping("prefix", "uri");
        verify(this.handler);
    }

    @Test
    public void testUnparsedEntityDecl() throws SAXException {
        this.handler.unparsedEntityDecl("name", "publicId", "systemId", "notationName");
        replay(this.handler);
        this.transformer.unparsedEntityDecl("name", "publicId", "systemId", "notationName");
        verify(this.handler);
    }
}
