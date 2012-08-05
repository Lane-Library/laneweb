package edu.stanford.irt.laneweb.classes;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class EventListTransformerTest {

    private Attributes attributes;

    private InputStream inputStream;

    private SAXParser saxParser;

    private Source source;

    private SourceResolver sourceResolver;

    private EventListTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.saxParser = createMock(SAXParser.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.transformer = new EventListTransformer(this.sourceResolver, this.saxParser);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer.setConsumer(this.xmlConsumer);
        this.attributes = createMock(Attributes.class);
        this.source = createMock(Source.class);
        this.inputStream = createMock(InputStream.class);
    }

    @Test
    public void testEndElement() throws SAXException {
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer);
        this.transformer.endElement("", "event", "event");
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer);
    }

    @Test
    public void testEndElementNotEvent() throws SAXException {
        this.xmlConsumer.endElement("", "notevent", "notevent");
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer);
        this.transformer.endElement("", "notevent", "notevent");
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer);
    }

    @Test
    public void testStartElement() throws SAXException, IOException {
        expect(this.attributes.getValue("href")).andReturn("value");
        expect(this.sourceResolver.resolveURI("value")).andReturn(this.source);
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        this.saxParser.parse(isA(InputSource.class), isA(XMLConsumer.class));
        this.inputStream.close();
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
        this.transformer.startElement("", "event", "event", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
    }

    @Test
    public void testStartElementNotEvent() throws SAXException, IOException {
        this.xmlConsumer.startElement("", "notevent", "notevent", this.attributes);
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
        this.transformer.startElement("", "notevent", "notevent", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
    }

    @Test
    public void testStartElementThrowIOException() throws SAXException, IOException {
        expect(this.attributes.getValue("href")).andReturn("value");
        expect(this.sourceResolver.resolveURI("value")).andThrow(new IOException());
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
        try {
            this.transformer.startElement("", "event", "event", this.attributes);
        } catch (LanewebException e) {
        }
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
    }

    @Test
    public void testStartElementThrowIOExceptionOnClose() throws SAXException, IOException {
        expect(this.attributes.getValue("href")).andReturn("value");
        expect(this.sourceResolver.resolveURI("value")).andReturn(this.source);
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        this.saxParser.parse(isA(InputSource.class), isA(XMLConsumer.class));
        this.inputStream.close();
        expectLastCall().andThrow(new IOException());
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
        try {
            this.transformer.startElement("", "event", "event", this.attributes);
        } catch (LanewebException e) {
        }
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
    }

    @Test
    public void testStartElementThrowSAXException() throws SAXException, IOException {
        expect(this.attributes.getValue("href")).andReturn("value");
        expect(this.sourceResolver.resolveURI("value")).andReturn(this.source);
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        this.saxParser.parse(isA(InputSource.class), isA(XMLConsumer.class));
        expectLastCall().andThrow(new SAXException());
        this.inputStream.close();
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
        try {
            this.transformer.startElement("", "event", "event", this.attributes);
        } catch (SAXException e) {
        }
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source, this.inputStream);
    }
}
