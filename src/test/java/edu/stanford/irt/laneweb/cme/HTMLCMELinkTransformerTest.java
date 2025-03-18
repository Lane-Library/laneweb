package edu.stanford.irt.laneweb.cme;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class HTMLCMELinkTransformerTest {

    private Attributes attributes;

    private Map<String, Object> model;

    private HTMLCMELinkTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.attributes = mock(Attributes.class);
        this.model = new HashMap<>();
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer = new HTMLCMELinkTransformer();
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement("uri", "element", "qName", this.attributes);
        replay(this.xmlConsumer, this.attributes);
        this.transformer.startElement("uri", "element", "qName", this.attributes);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementAnchorCme() throws SAXException {
        this.xmlConsumer.startElement(eq("uri"), eq("a"), eq("qName"), isA(AttributesImpl.class));
        replay(this.xmlConsumer);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("uri", "href", "href", "type", "http://www.uptodate.com/online");
        this.transformer.startElement("uri", "a", "qName", atts);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementAnchorNoCme() throws SAXException {
        expect(this.attributes.getValue("href")).andReturn("value");
        this.xmlConsumer.startElement("uri", "a", "qName", this.attributes);
        replay(this.xmlConsumer, this.attributes);
        this.transformer.startElement("uri", "a", "qName", this.attributes);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementAnchorNoCmeHost() throws SAXException {
        this.xmlConsumer.startElement(eq("uri"), eq("a"), eq("qName"), isA(AttributesImpl.class));
        replay(this.xmlConsumer);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("uri", "href", "href", "type", "http://foo");
        this.transformer.startElement("uri", "a", "qName", atts);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementAnchorNoHttp() throws SAXException {
        this.xmlConsumer.startElement(eq("uri"), eq("a"), eq("qName"), isA(AttributesImpl.class));
        replay(this.xmlConsumer);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("uri", "href", "href", "type", "ftp://www.uptodate.com/online");
        this.transformer.startElement("uri", "a", "qName", atts);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementInputEmrid() throws SAXException {
        this.model.put(Model.EMRID, "emrid");
        this.transformer.setModel(this.model);
        this.xmlConsumer.startElement(eq("uri"), eq("input"), eq("qName"), isA(Attributes.class));
        replay(this.xmlConsumer);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("uri", "value", "value", "type", "{emrid}");
        this.transformer.startElement("uri", "input", "qName", atts);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementInputNoEmrid() throws SAXException {
        this.model.put(Model.EMRID, "emrid");
        this.transformer.setModel(this.model);
        this.xmlConsumer.startElement(eq("uri"), eq("input"), eq("qName"), isA(Attributes.class));
        replay(this.xmlConsumer);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("uri", "value", "value", "type", "not-emrid-replacement-string");
        this.transformer.startElement("uri", "input", "qName", atts);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElementInputNullValue() throws SAXException {
        this.xmlConsumer.startElement(eq("uri"), eq("input"), eq("qName"), isA(Attributes.class));
        replay(this.xmlConsumer);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("uri", "value", "value", "type", null);
        this.transformer.startElement("uri", "input", "qName", atts);
        verify(this.xmlConsumer);
    }
}
