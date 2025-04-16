package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.text.IsEqualCompressingWhiteSpace.equalToCompressingWhiteSpace;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class HTMLSAXParserTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private NekoHTMLConfiguration configuration;

    private InputStream inputStream;

    private LexicalHandler lexicalHandler;

    private HTMLSAXParser parser;

    private Source source;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        Map<String, String> props = new HashMap<>();
        props.put("http://cyberneko.org/html/properties/default-encoding", UTF_8);
        props.put("http://cyberneko.org/html/properties/names/elems", "lower");
        props.put("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        this.configuration = new NekoHTMLConfiguration(props,
                Collections.singletonMap("http://cyberneko.org/html/features/insert-namespaces", Boolean.TRUE));
        this.parser = new HTMLSAXParser(this.configuration);
        this.xmlConsumer = new TestXMLConsumer();
        this.source = mock(Source.class);
        this.inputStream = new ByteArrayInputStream("<html><title>foo<table><td>bar".getBytes());
        this.lexicalHandler = mock(LexicalHandler.class);
    }

    @Test
    public void testParseSourceXMLConsumer() throws SAXException, IOException {
        expect(this.source.getURI()).andReturn("uri");
        expect(this.source.getInputStream()).andReturn(this.inputStream);
        replay(this.source, this.lexicalHandler);
        this.parser.parse(this.source, this.xmlConsumer);
        assertTrue(equalToCompressingWhiteSpace(this.xmlConsumer.getExpectedResult(this, "html.xml"))
                .matches(this.xmlConsumer.getStringValue()));
        verify(this.source, this.lexicalHandler);
    }
}
