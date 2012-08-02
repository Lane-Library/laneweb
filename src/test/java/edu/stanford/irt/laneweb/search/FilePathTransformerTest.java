package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class FilePathTransformerTest {

    private Attributes attributes;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    private FilePathTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.saxParser = createMock(SAXParser.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.transformer = new FilePathTransformer(this.sourceResolver, this.saxParser);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer.setConsumer(this.xmlConsumer);
        this.attributes = createMock(Attributes.class);
    }

    @Test
    public void testEndElementFile() throws SAXException {
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        this.transformer.endElement("", "file", "file");
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testEndElementNotFile() throws SAXException {
        this.xmlConsumer.endElement("", "foo", "foo");
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        this.transformer.endElement("", "foo", "foo");
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testGetKey() {
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        assertEquals("file-path", this.transformer.getKey());
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testGetType() {
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        assertEquals("file-path", this.transformer.getType());
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testGetValidity() {
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        assertEquals(SourceValidity.VALID, this.transformer.getValidity().isValid());
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testStartElementFile() throws SAXException, IOException {
        Source source = createMock(Source.class);
        SourceValidity validity = createMock(SourceValidity.class);
        expect(this.attributes.getValue("path")).andReturn("path");
        expect(this.sourceResolver.resolveURI("file:path")).andReturn(source);
        expect(source.getValidity()).andReturn(validity);
        InputStream inputStream = createMock(InputStream.class);
        expect(source.getInputStream()).andReturn(inputStream);
        expect(source.getURI()).andReturn("uri");
        this.saxParser.parse(isA(InputSource.class), isA(XMLConsumer.class), isA(XMLConsumer.class));
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, source, validity, inputStream);
        this.transformer.startElement("", "file", "file", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, source, validity, inputStream);
    }

    @Test
    public void testStartElementFileThrowsIOException() throws SAXException, IOException {
        Source source = createMock(Source.class);
        SourceValidity validity = createMock(SourceValidity.class);
        expect(this.attributes.getValue("path")).andReturn("path");
        expect(this.sourceResolver.resolveURI("file:path")).andReturn(source);
        expect(source.getValidity()).andReturn(validity);
        InputStream inputStream = createMock(InputStream.class);
        expect(source.getInputStream()).andThrow(new IOException());
        // expect(source.getURI()).andReturn("uri");
        // this.saxParser.parse(isA(InputSource.class), isA(XMLConsumer.class),
        // isA(XMLConsumer.class));
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, source, validity, inputStream);
        try {
            this.transformer.startElement("", "file", "file", this.attributes);
        } catch (LanewebException e) {
        }
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, source, validity, inputStream);
    }

    @Test
    public void testStartElementNotFile() throws SAXException {
        this.xmlConsumer.startElement("", "foo", "foo", this.attributes);
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        this.transformer.startElement("", "foo", "foo", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }
}
