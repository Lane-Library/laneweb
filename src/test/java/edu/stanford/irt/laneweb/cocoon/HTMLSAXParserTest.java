package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class HTMLSAXParserTest {

    private NekoHTMLConfiguration configuration;

    private InputStream inputStream;

    private LexicalHandler lexicalHandler;

    private HTMLSAXParser parser;

    private Source source;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        Map<String, String> props = new HashMap<>();
        props.put("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        props.put("http://cyberneko.org/html/properties/names/elems", "lower");
        props.put("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        this.configuration = new NekoHTMLConfiguration(props,
                Collections.singletonMap("http://cyberneko.org/html/features/insert-namespaces", Boolean.TRUE));
        this.parser = new HTMLSAXParser(this.configuration);
        this.xmlConsumer = new TestXMLConsumer();
        this.source = createMock(Source.class);
        this.inputStream = new ByteArrayInputStream("<html><title>foo<table><td>bar".getBytes());
        this.lexicalHandler = createMock(LexicalHandler.class);
    }

    @Test
    public void testParseSourceXMLConsumer() throws SAXException, IOException {
        expect(this.source.getURI()).andReturn("uri");
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        replay(this.source, this.lexicalHandler);
        this.parser.parse(this.source, this.xmlConsumer);
        assertTrue(equalToIgnoringWhiteSpace(this.xmlConsumer.getExpectedResult(this, "html.xml"))
                .matches(this.xmlConsumer.getStringValue()));
        System.out.println(this.xmlConsumer.getStringValue());
        verify(this.source, this.lexicalHandler);
    }
}
