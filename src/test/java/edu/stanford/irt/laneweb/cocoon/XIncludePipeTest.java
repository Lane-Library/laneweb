package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.cocoon.components.source.impl.MultiSourceValidity;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class XIncludePipeTest {

    private Attributes attributes;

    private XIncludePipe pipe;

    private SourceResolver sourceResolver;

    private XMLConsumer xmlConsumer;
    
    private MultiSourceValidity validity;

    @Before
    public void setUp() {
        this.sourceResolver = createMock(SourceResolver.class);
        this.validity = createMock(MultiSourceValidity.class);
        this.attributes = createMock(Attributes.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.pipe = new XIncludePipe(this.sourceResolver, this.validity, null, null);
        //TODO: have init make sense
        this.pipe.init(null, null);
        this.pipe.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        char[] characters = new char[0];
        this.xmlConsumer.characters(characters, 0, 0);
        replay(this.xmlConsumer);
        this.pipe.characters(characters, 0, 0);
        verify(this.xmlConsumer);
    }

    @Test
    public void testComment() throws SAXException {
        char[] comment = new char[0];
        this.xmlConsumer.comment(comment, 0, 0);
        replay(this.xmlConsumer);
        this.pipe.comment(comment, 0, 0);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndCDATA() throws SAXException {
        this.xmlConsumer.endCDATA();
        replay(this.xmlConsumer);
        this.pipe.endCDATA();
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndDocument() throws SAXException {
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.pipe.endDocument();
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndElementStringStringString() throws SAXException {
        this.xmlConsumer.endElement("uri", "localname", "qname");
        replay(this.xmlConsumer);
        this.pipe.endElement("uri", "localname", "qname");
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndEntityString() throws SAXException {
        this.xmlConsumer.endEntity("name");
        replay(this.xmlConsumer);
        this.pipe.endEntity("name");
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndPrefixMappingString() throws SAXException {
        this.xmlConsumer.endPrefixMapping("prefix");
        replay(this.xmlConsumer);
        this.pipe.endPrefixMapping("prefix");
        verify(this.xmlConsumer);
    }

    @Test
    public void testIgnorableWhitespace() throws SAXException {
        char[] characters = new char[0];
        this.xmlConsumer.ignorableWhitespace(characters, 0, 0);
        replay(this.xmlConsumer);
        this.pipe.ignorableWhitespace(characters, 0, 0);
        verify(this.xmlConsumer);
    }

    @Test
    public void testProcessingInstructionStringString() throws SAXException {
        this.xmlConsumer.processingInstruction("target", "data");
        replay(this.xmlConsumer);
        this.pipe.processingInstruction("target", "data");
        verify(this.xmlConsumer);
    }

    @Test
    public void testSetDocumentLocatorLocator() {
        Locator locator = createMock(Locator.class);
        this.xmlConsumer.setDocumentLocator(locator);
        replay(this.xmlConsumer);
        this.pipe.setDocumentLocator(locator);
        verify(this.xmlConsumer);
    }

    @Test
    public void testSkippedEntityString() throws SAXException {
        this.xmlConsumer.skippedEntity("name");
        replay(this.xmlConsumer);
        this.pipe.skippedEntity("name");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartCDATA() throws SAXException {
        this.xmlConsumer.startCDATA();
        replay(this.xmlConsumer);
        this.pipe.startCDATA();
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementStringStringStringAttributes() throws SAXException {
        this.xmlConsumer.startElement("uri", "localname", "qName", this.attributes);
        replay(this.xmlConsumer);
        this.pipe.startElement("uri", "localname", "qName", this.attributes);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartEntityString() throws SAXException {
        this.xmlConsumer.startEntity("name");
        replay(this.xmlConsumer);
        this.pipe.startEntity("name");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartPrefixMappingStringString() throws SAXException {
        this.xmlConsumer.startPrefixMapping("prefix", "uri");
        replay(this.xmlConsumer);
        this.pipe.startPrefixMapping("prefix", "uri");
        verify(this.xmlConsumer);
    }

    // This test demonstrated a bug where an exception occurred during include
    // processing but
    // before the try/catch block incrementing the fallback state.
    @Test
    public void testStartXIncludeElement() throws MalformedURLException, IOException, SAXException {
        expect(this.attributes.getValue("http://www.w3.org/XML/1998/namespace", "base")).andReturn(null);
        expect(this.attributes.getValue("", "href")).andReturn("foo");
        expect(this.attributes.getValue("", "parse")).andReturn(null);
        expect(this.attributes.getValue("", "xpointer")).andReturn(null);
        expect(this.sourceResolver.resolveURI("foo")).andThrow(new LanewebException("oopsie"));
        replay(this.sourceResolver, this.attributes);
        // must call init() to create xmlBaseSupport object
        try {
            this.pipe.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
        } catch (Exception e) {
            fail("should catch all exceptions");
        }
        verify(this.sourceResolver, this.attributes);
    }
}
