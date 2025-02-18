package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class SearchDirectoryTransformerTest {

    private SearchDirectoryTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.transformer = new SearchDirectoryTransformer();
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.transformer
                .setParameters(Collections.singletonMap("directories", "file:" + System.getProperty("user.dir")));
    }

    @Test
    public void testEndDocument() throws SAXException {
        this.xmlConsumer.startElement(eq(""), eq("file"), eq("file"), isA(Attributes.class));
        expectLastCall().anyTimes();
        this.xmlConsumer.endElement("", "file", "file");
        expectLastCall().anyTimes();
        this.xmlConsumer.endElement("http://lane.stanford.edu/search-templates/ns", "search-templates",
                "search-templates");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.transformer.endDocument();
        verify(this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        assertEquals("search-directory", this.transformer.getKey());
    }

    @Test
    public void testGetType() {
        assertEquals("search-directory", this.transformer.getType());
    }

    @Test
    public void testGetValidity() {
        assertTrue(this.transformer.getValidity().isValid());
    }

    @Test
    public void testStartDocument() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/search-templates/ns"), eq("search-templates"),
                eq("search-templates"), isA(Attributes.class));
        replay(this.xmlConsumer);
        this.transformer.startDocument();
        verify(this.xmlConsumer);
    }
}
