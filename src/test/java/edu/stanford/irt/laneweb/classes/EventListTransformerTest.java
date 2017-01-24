package edu.stanford.irt.laneweb.classes;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class EventListTransformerTest {

    private Attributes attributes;

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
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.attributes = createMock(Attributes.class);
        this.source = createMock(Source.class);
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
    public void testGetValidity() {
        assertSame(AlwaysValid.SHARED_INSTANCE, this.transformer.getValidity());
    }

    @Test
    public void testStartElement() throws SAXException, IOException, URISyntaxException {
        expect(this.attributes.getValue("href")).andReturn("value");
        expect(this.sourceResolver.resolveURI(new URI("value"))).andReturn(this.source);
        this.saxParser.parse(eq(this.source), isA(XMLConsumer.class));
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source);
        this.transformer.startElement("", "event", "event", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source);
    }

    @Test
    public void testStartElementNotEvent() throws SAXException, IOException {
        this.xmlConsumer.startElement("", "notevent", "notevent", this.attributes);
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source);
        this.transformer.startElement("", "notevent", "notevent", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source);
    }

    @Test(expected = LanewebException.class)
    public void testStartElementURISyntaxException() throws SAXException, IOException, URISyntaxException {
        expect(this.attributes.getValue("href")).andReturn("/{}");
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source);
        this.transformer.startElement("", "event", "event", this.attributes);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer, this.attributes, this.source);
    }
}
