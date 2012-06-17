package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SearchDirectoryTransformerTest {

    private SearchDirectoryTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new SearchDirectoryTransformer();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer.setConsumer(this.xmlConsumer);
        this.transformer.setParameters(Collections.singletonMap("directories", "file:" + System.getProperty("user.dir")));
    }

    @Test
    public void testEndDocument() throws SAXException {
        this.xmlConsumer.startElement(eq(""), eq("file"), eq("file"), isA(Attributes.class));
        expectLastCall().atLeastOnce();
        this.xmlConsumer.endElement("", "file", "file");
        expectLastCall().atLeastOnce();
        this.xmlConsumer.endElement("http://lane.stanford.edu/search-templates/ns", "search-templates", "search-templates");
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
        assertEquals(SourceValidity.VALID, this.transformer.getValidity().isValid());
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
