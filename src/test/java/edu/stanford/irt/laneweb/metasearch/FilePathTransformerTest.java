package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.Cacheable;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class FilePathTransformerTest {

    private interface CacheableSource extends Cacheable, Source {
    }

    private Attributes attributes;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    private FilePathTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.saxParser = mock(SAXParser.class);
        this.sourceResolver = mock(SourceResolver.class);
        this.transformer = new FilePathTransformer(this.sourceResolver, this.saxParser);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.attributes = mock(Attributes.class);
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
        assertTrue(this.transformer.getValidity().isValid());
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }

    @Test
    public void testStartElementFile() throws SAXException, IOException, URISyntaxException {
        CacheableSource source = mock(CacheableSource.class);
        Validity validity = mock(Validity.class);
        expect(this.attributes.getValue("path")).andReturn("path");
        expect(this.sourceResolver.resolveURI(new URI("file:path"))).andReturn(source);
        expect(source.getValidity()).andReturn(validity);
        this.saxParser.parse(isA(Source.class), isA(XMLConsumer.class));
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, source, validity);
        this.transformer.startElement("", "file", "file", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, source, validity);
    }

    @Test
    public void testStartElementNotFile() throws SAXException {
        this.xmlConsumer.startElement("", "foo", "foo", this.attributes);
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
        this.transformer.startElement("", "foo", "foo", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes);
    }
}
