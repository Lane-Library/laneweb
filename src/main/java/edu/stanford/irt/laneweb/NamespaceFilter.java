package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class NamespaceFilter implements Transformer {

    private XMLConsumer consumer;

    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.consumer.characters(ch, start, length);
    }

    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        this.consumer.comment(ch, start, length);
    }

    public void endCDATA() throws SAXException {
        this.consumer.endCDATA();
    }

    public void endDocument() throws SAXException {
        this.consumer.endDocument();
    }

    public void endDTD() throws SAXException {
        this.consumer.endDTD();
    }

    public void endElement(final String uri, final String localName, final String name) throws SAXException {
        this.consumer.endElement(uri, localName, name);
    }

    public void endEntity(final String name) throws SAXException {
        this.consumer.endEntity(name);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        if ("".equals(prefix)) {
            this.consumer.endPrefixMapping(prefix);
        }
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        this.consumer.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(final String target, final String data) throws SAXException {
        this.consumer.processingInstruction(target, data);
    }

    public void setConsumer(final XMLConsumer consumer) {
        this.consumer = consumer;
    }

    public void setDocumentLocator(final Locator locator) {
        this.consumer.setDocumentLocator(locator);
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters arg3) throws ProcessingException, SAXException,
            IOException {
    }

    public void skippedEntity(final String name) throws SAXException {
        this.consumer.skippedEntity(name);
    }

    public void startCDATA() throws SAXException {
        this.consumer.startCDATA();
    }

    public void startDocument() throws SAXException {
        this.consumer.startDocument();
    }

    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        this.consumer.startDTD(name, publicId, systemId);
    }

    public void startElement(final String uri, final String localName, final String name, final Attributes atts) throws SAXException {
        this.consumer.startElement(uri, localName, name, atts);
    }

    public void startEntity(final String name) throws SAXException {
        this.consumer.startEntity(name);
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if ("".equals(prefix) && "http://www.w3.org/1999/xhtml".equals(uri)) {
            this.consumer.startPrefixMapping(prefix, uri);
        }
    }
}
