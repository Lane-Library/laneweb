package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.StringReader;

import org.apache.cocoon.xml.ContentHandlerWrapper;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TextNodeParsingTransformer extends AbstractTransformer {

    private static class HtmlSAXParser extends AbstractSAXParser {

        protected HtmlSAXParser(final HTMLConfiguration conf) {
            super(conf);
        }
    }

    private StringBuilder content = new StringBuilder();

    private final String elementName = "event_description";

    private boolean inElement = false;

    private final String namespace = "http://www.w3.org/1999/xhtml";

    AbstractSAXParser htmlParser;

    public TextNodeParsingTransformer() {
        HTMLConfiguration conf = new HTMLConfiguration();
        conf.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        conf.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
        conf.setFeature("http://cyberneko.org/html/features/insert-namespaces", true);
        conf.setProperty("http://cyberneko.org/html/properties/namespaces-uri", this.namespace);
        this.htmlParser = new HtmlSAXParser(conf);
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (this.inElement) {
            this.content.append(ch, start, length);
        } else {
            this.xmlConsumer.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (this.elementName.equals(qName)) {
            this.inElement = false;
            StringReader stringReader = new StringReader(this.content.toString());
            InputSource inputSource = new InputSource(stringReader);
            XMLConsumer xmlConsumer = new ContentHandlerWrapper(this.xmlConsumer) {

                @Override
                public void endDocument() {
                    // Do nothing
                }

                @Override
                public void processingInstruction(final String target, final String data) {
                }

                @Override
                public void startDocument() {
                }
            };
            try {
                this.htmlParser.setContentHandler(xmlConsumer);
                this.htmlParser.parse(inputSource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.content = new StringBuilder();
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    /**
     * @return the elementname
     */
    public String getElementName() {
        return this.elementName;
    }

    @Override
    public void startDocument() throws SAXException {
        this.xmlConsumer.startDocument();
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        this.xmlConsumer.startElement(uri, localName, qName, atts);
        if (this.elementName.equals(qName)) {
            this.inElement = true;
        }
    }
}
