package edu.stanford.irt.laneweb.resource;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class AbstractXHTMLSAXStrategyTest {

    private static class TestAbstractXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<Object> {

        @Override
        public void toSAX(final Object object, final XMLConsumer xmlConsumer) {
        }
    }

    private AbstractXHTMLSAXStrategy<Object> strategy;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.strategy = new TestAbstractXHTMLSAXStrategy();
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testCreateAnchor() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        replay(this.xmlConsumer);
        this.strategy.createAnchor(this.xmlConsumer, "href", "text");
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateAnchorWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        replay(this.xmlConsumer);
        this.strategy.createAnchorWithClass(this.xmlConsumer, "href", "class", "text");
        assertEquals("class", attributes.getValue().getValue("class"));
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateAnchorWithClassAndTitle() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        replay(this.xmlConsumer);
        this.strategy.createAnchorWithClassAndTitle(this.xmlConsumer, "href", "class", "title", "text");
        assertEquals("class", attributes.getValue().getValue("class"));
        assertEquals("title", attributes.getValue().getValue("title"));
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateAnchorWithTitle() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        replay(this.xmlConsumer);
        this.strategy.createAnchorWithTitle(this.xmlConsumer, "href", "title", "text");
        assertEquals("title", attributes.getValue().getValue("title"));
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateDivWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "div", "div");
        replay(this.xmlConsumer);
        this.strategy.createDivWithClass(this.xmlConsumer, "class", "text");
        assertEquals("class", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateDivWithNullClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "div", "div");
        replay(this.xmlConsumer);
        this.strategy.createDivWithClass(this.xmlConsumer, null, "text");
        assertEquals("", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateSpan() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("span"), eq("span"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "span", "span");
        replay(this.xmlConsumer);
        this.strategy.createSpan(this.xmlConsumer, "text");
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateSpanWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("span"), eq("span"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "span", "span");
        replay(this.xmlConsumer);
        this.strategy.createSpanWithClass(this.xmlConsumer, "class", "text");
        assertEquals("class", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateSpanWithId() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("span"), eq("span"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "span", "span");
        replay(this.xmlConsumer);
        this.strategy.createSpanWithId(this.xmlConsumer, "id", "text");
        assertEquals("id", attributes.getValue().getValue("id"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateSpanWithNullId() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("span"), eq("span"), capture(attributes));
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "span", "span");
        replay(this.xmlConsumer);
        this.strategy.createSpanWithId(this.xmlConsumer, null, "text");
        assertEquals("", attributes.getValue().getValue("id"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testCreateTitle() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("title"), eq("title"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("title".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "title", "title");
        replay(this.xmlConsumer);
        this.strategy.createTitle(this.xmlConsumer, "title");
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndAnchor() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        replay(this.xmlConsumer);
        this.strategy.endAnchor(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndBody() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "body", "body");
        replay(this.xmlConsumer);
        this.strategy.endBody(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndDiv() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "div", "div");
        replay(this.xmlConsumer);
        this.strategy.endDiv(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndHead() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "head", "head");
        replay(this.xmlConsumer);
        this.strategy.endHead(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndHTMLDocument() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "html", "html");
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.strategy.endHTMLDocument(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndLi() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        replay(this.xmlConsumer);
        this.strategy.endLi(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndUl() throws SAXException {
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        replay(this.xmlConsumer);
        this.strategy.endUl(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchor() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchor(this.xmlConsumer, "href");
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorNullHref() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchor(this.xmlConsumer, null);
        assertEquals("", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithClass(this.xmlConsumer, "href", "class");
        assertEquals("class", attributes.getValue().getValue("class"));
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorWithClassAndTitle() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithClassAndTitle(this.xmlConsumer, "href", "class", "title");
        assertEquals("title", attributes.getValue().getValue("title"));
        assertEquals("class", attributes.getValue().getValue("class"));
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorWithClassAndTitleNullAtts() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithClassAndTitle(this.xmlConsumer, null, null, null);
        assertEquals("", attributes.getValue().getValue("title"));
        assertEquals("", attributes.getValue().getValue("class"));
        assertEquals("", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorWithClassNullAtts() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithClass(this.xmlConsumer, null, null);
        assertEquals("", attributes.getValue().getValue("class"));
        assertEquals("", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorWithTitle() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithTitle(this.xmlConsumer, "href", "title");
        assertEquals("title", attributes.getValue().getValue("title"));
        assertEquals("href", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartAnchorWithTitleNullAtts() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithTitle(this.xmlConsumer, null, null);
        assertEquals("", attributes.getValue().getValue("title"));
        assertEquals("", attributes.getValue().getValue("href"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartBody() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("body"), eq("body"),
                isA(Attributes.class));
        replay(this.xmlConsumer);
        this.strategy.startBody(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartDiv() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), isA(Attributes.class));
        replay(this.xmlConsumer);
        this.strategy.startDiv(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartDivWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startDivWithClass(this.xmlConsumer, "class");
        assertEquals("class", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartDivWithNullClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startDivWithClass(this.xmlConsumer, null);
        assertEquals("", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartHead() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("head"), eq("head"),
                isA(Attributes.class));
        replay(this.xmlConsumer);
        this.strategy.startHead(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartHTMLDocument() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("html"), eq("html"),
                isA(Attributes.class));
        replay(this.xmlConsumer);
        this.strategy.startHTMLDocument(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartLi() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        replay(this.xmlConsumer);
        this.strategy.startLi(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartLiWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startLiWithClass(this.xmlConsumer, "class");
        assertEquals("class", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartUl() throws SAXException {
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        replay(this.xmlConsumer);
        this.strategy.startUl(this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartUlWithClass() throws SAXException {
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startUlWithClass(this.xmlConsumer, "class");
        assertEquals("class", attributes.getValue().getValue("class"));
        verify(this.xmlConsumer);
    }
}
