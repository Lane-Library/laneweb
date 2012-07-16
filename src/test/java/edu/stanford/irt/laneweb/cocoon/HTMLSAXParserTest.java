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
import java.io.InputStreamReader;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class HTMLSAXParserTest {

    private NekoHTMLConfiguration configuration;

    private ContentHandler contentHandler;

    private InputSource inputSource;

    private InputStream inputStream;

    private LexicalHandler lexicalHandler;

    private HTMLSAXParser parser;

    @Before
    public void setUp() throws Exception {
        //TODO: set up with the features/properties we actually use
        this.configuration = new NekoHTMLConfiguration(Collections.<String, String>emptyMap(), Collections.<String, Boolean>emptyMap());
        this.parser = new HTMLSAXParser(this.configuration);
        this.contentHandler = createMock(ContentHandler.class);
        this.inputSource = createMock(InputSource.class);
        this.inputStream = new ByteArrayInputStream("<html/>".getBytes());
        this.lexicalHandler = createMock(LexicalHandler.class);
    }

    @Test
    public void testParseInputSourceContentHandler() throws SAXException, IOException {
        expect(this.inputSource.getPublicId()).andReturn("public id");
        expect(this.inputSource.getSystemId()).andReturn("system id");
        expect(this.inputSource.getByteStream()).andReturn(this.inputStream);
        expect(this.inputSource.getCharacterStream()).andReturn(new InputStreamReader(this.inputStream, "UTF-8"));
        expect(this.inputSource.getEncoding()).andReturn("UTF-8");
        this.contentHandler.setDocumentLocator(isA(Locator.class));
        this.contentHandler.startDocument();
        this.contentHandler.startElement(eq(""), eq("HTML"), eq("HTML"), isA(Attributes.class));
        this.contentHandler.endElement("", "HTML", "HTML");
        this.contentHandler.endDocument();
        replay(this.contentHandler, this.inputSource, this.lexicalHandler);
        this.parser.parse(this.inputSource, this.contentHandler);
        verify(this.contentHandler, this.inputSource, this.lexicalHandler);
    }

    @Test
    public void testParseInputSourceContentHandlerLexicalHandler() throws SAXException, IOException {
        expect(this.inputSource.getPublicId()).andReturn("public id");
        expect(this.inputSource.getSystemId()).andReturn("system id");
        expect(this.inputSource.getByteStream()).andReturn(this.inputStream);
        expect(this.inputSource.getCharacterStream()).andReturn(new InputStreamReader(this.inputStream, "UTF-8"));
        expect(this.inputSource.getEncoding()).andReturn("UTF-8");
        this.contentHandler.setDocumentLocator(isA(Locator.class));
        this.contentHandler.startDocument();
        this.contentHandler.startElement(eq(""), eq("HTML"), eq("HTML"), isA(Attributes.class));
        this.contentHandler.endElement("", "HTML", "HTML");
        this.contentHandler.endDocument();
        replay(this.contentHandler, this.inputSource, this.lexicalHandler);
        this.parser.parse(this.inputSource, this.contentHandler, this.lexicalHandler);
        verify(this.contentHandler, this.inputSource, this.lexicalHandler);
    }
}
