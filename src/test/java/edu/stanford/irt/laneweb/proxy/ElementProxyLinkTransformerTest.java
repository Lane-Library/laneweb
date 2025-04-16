package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class ElementProxyLinkTransformerTest {

    private ElementProxyLinkTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.transformer = new ElementProxyLinkTransformer("elementName");
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.xmlConsumer.characters(aryEq("foo".toCharArray()), eq(0), eq("foo".length()));
        replay(this.xmlConsumer);
        this.transformer.characters("foo".toCharArray(), 0, "foo".length());
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.xmlConsumer.endElement("", "", "");
        replay(this.xmlConsumer);
        this.transformer.endElement("", "", "");
        verify(this.xmlConsumer);
    }

    @Test
    public void testInElement() throws SAXException {
        this.transformer.setModel(Collections.singletonMap(Model.BASE_PROXY_URL, "url"));
        this.xmlConsumer.startElement("", "elementName", "", null);
        this.xmlConsumer.characters(aryEq("urlfoobar".toCharArray()), eq(0), eq("urlfoobar".length()));
        this.xmlConsumer.endElement("", "elementName", "");
        replay(this.xmlConsumer);
        this.transformer.startElement("", "elementName", "", null);
        this.transformer.characters("foo".toCharArray(), 0, "foo".length());
        this.transformer.characters("bar".toCharArray(), 0, "bar".length());
        this.transformer.endElement("", "elementName", "");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement("", "", "", null);
        replay(this.xmlConsumer);
        this.transformer.startElement("", "", "", null);
        verify(this.xmlConsumer);
    }
}
