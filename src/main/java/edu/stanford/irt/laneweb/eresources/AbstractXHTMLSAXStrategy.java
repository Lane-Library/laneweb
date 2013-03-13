package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;

public abstract class AbstractXHTMLSAXStrategy<T extends Object> implements SAXStrategy<T> {

    private static final String A = "a";

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String EMPTY_NS = "";
    
    private static final String NO_PREFIX = "";

    private static final String HREF = "href";

    private static final String LI = "li";

    private static final String SPAN = "span";

    private static final String UL = "ul";

    private static final String TITLE = "title";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private static final String HEAD = "head";

    private static final String BODY = "body";
    
    protected void startHTMLDocument(XMLConsumer xmlConsumer) throws SAXException {
        xmlConsumer.startDocument();
        xmlConsumer.startPrefixMapping(NO_PREFIX, XHTML_NS);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "html");
    }
    
    protected void endHTMLDocument(XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "html");
        xmlConsumer.endPrefixMapping(NO_PREFIX);
        xmlConsumer.endDocument();
    }
    
    protected void startHead(XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.startElement(xmlConsumer, XHTML_NS, HEAD);
    }
    
    protected void endHead(XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, HEAD);
    }
    
    protected void startBody(XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.startElement(xmlConsumer, XHTML_NS, BODY);
    }
    
    protected void endBody(XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, BODY);
    }
    
    protected void createTitle(XMLConsumer xmlConsumer, String title) throws SAXException {
        XMLUtils.createElementNS(xmlConsumer, XHTML_NS, TITLE, title);
    }

    protected void createAnchor(final XMLConsumer xmlConsumer, final String href, final String text)
            throws SAXException {
        startAnchor(xmlConsumer, href);
        XMLUtils.data(xmlConsumer, text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
    }

    protected void createAnchorWithClass(final XMLConsumer xmlConsumer, final String href, final String clazz,
            final String text) throws SAXException {
        startAnchorWithClass(xmlConsumer, href, clazz);
        XMLUtils.data(xmlConsumer, text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
    }

    protected void createAnchorWithTitle(final XMLConsumer xmlConsumer, final String href, final String title,
            final String text) throws SAXException {
        startAnchorWithTitle(xmlConsumer, href, title);
        XMLUtils.data(xmlConsumer, text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
    }

    protected void createSpan(final XMLConsumer xmlConsumer, final String text) throws SAXException {
        createElement(xmlConsumer, SPAN, text);
    }

    protected void createSpanWithClass(final XMLConsumer xmlConsumer, final String clazz, final String text)
            throws SAXException {
        createElementWithClass(xmlConsumer, SPAN, clazz, text);
    }

    protected void createDivWithClass(final XMLConsumer xmlConsumer, final String clazz, final String text)
            throws SAXException {
        createElementWithClass(xmlConsumer, DIV, clazz, text);
    }

    protected void endAnchor(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
    }

    protected void endDiv(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
    }

    protected void endLi(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
    }

    protected void endUl(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
    }

    protected void startAnchor(final XMLConsumer xmlConsumer, final String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, href);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
    }

    protected void startAnchorWithClass(final XMLConsumer xmlConsumer, final String href, final String clazz)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, clazz);
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, href);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
    }

    protected void startAnchorWithTitle(final XMLConsumer xmlConsumer, final String href, final String title)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, href);
        atts.addAttribute(EMPTY_NS, TITLE, TITLE, CDATA, title);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
    }

    protected void startDivWithClass(final XMLConsumer xmlConsumer, final String clazz) throws SAXException {
        startElementWithClass(xmlConsumer, DIV, clazz);
    }

    protected void startLi(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
    }

    protected void startLiWithClass(final XMLConsumer xmlConsumer, final String clazz) throws SAXException {
        startElementWithClass(xmlConsumer, LI, clazz);
    }

    protected void startUl(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.startElement(xmlConsumer, XHTML_NS, UL);
    }

    protected void startDiv(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV);
    }

    protected void startUlWithClass(final XMLConsumer xmlConsumer, final String clazz) throws SAXException {
        startElementWithClass(xmlConsumer, UL, clazz);
    }

    private void createElement(final XMLConsumer xmlConsumer, final String name, final String text) throws SAXException {
        XMLUtils.createElementNS(xmlConsumer, XHTML_NS, name, text);
    }

    private void createElementWithClass(final XMLConsumer xmlConsumer, final String name, final String clazz,
            final String text) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, clazz);
        XMLUtils.createElement(xmlConsumer, XHTML_NS, name, atts, text);
    }

    private void startElementWithClass(final XMLConsumer xmlConsumer, final String name, final String clazz)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, clazz);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, name, atts);
    }
}
