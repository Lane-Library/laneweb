package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class HTMLSAXParserTest {

    private NekoHTMLConfiguration configuration;

    private InputStream inputStream;

    private LexicalHandler lexicalHandler;

    private HTMLSAXParser parser;

    private Source source;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        // TODO: set up with the features/properties we actually use
        this.configuration = new NekoHTMLConfiguration(Collections.emptyMap(),
                Collections.emptyMap());
        this.parser = new HTMLSAXParser(this.configuration);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.source = createMock(Source.class);
        this.inputStream = new ByteArrayInputStream("<html/>".getBytes());
        this.lexicalHandler = createMock(LexicalHandler.class);
    }

    @Test
    public void testParseSourceXMLConsumer() throws SAXException, IOException {
        expect(this.source.getURI()).andReturn("uri");
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        this.xmlConsumer.setDocumentLocator(isA(Locator.class));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("HTML"), eq("HTML"), isA(Attributes.class));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "HTML", "HTML");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.source, this.lexicalHandler);
        this.parser.parse(this.source, this.xmlConsumer);
        verify(this.xmlConsumer, this.source, this.lexicalHandler);
    }
}
