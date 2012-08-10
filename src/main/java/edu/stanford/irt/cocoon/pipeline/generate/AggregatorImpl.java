package edu.stanford.irt.cocoon.pipeline.generate;

import java.io.IOException;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.sitemap.DefaultContentAggregator;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AggregatorImpl extends DefaultContentAggregator {

    private SAXParser saxParser;

    public AggregatorImpl(final SAXParser saxParser) {
        this.saxParser = saxParser;
    }

    @Override
    public void generate() throws IOException, SAXException {
        this.contentHandler.startDocument();
        startElem(this.rootElement);
        for (int i = 0; i < this.parts.size(); i++) {
            final Part part = (Part) this.parts.get(i);
            // this.rootElementIndex = part.stripRootElement ? -1 : 0;
            if (part.element != null) {
                this.currentElement = part.element;
                startElem(part.element);
            } else {
                this.currentElement = this.rootElement;
            }
            if (part.source instanceof XMLizable) {
                ((XMLizable) part.source).toSAX(this);
            } else {
                InputSource inputSource = new InputSource();
                try {
                    inputSource.setByteStream(part.source.getInputStream());
                    inputSource.setSystemId(part.uri);
                    this.saxParser.parse(inputSource, this, this);
                } finally {
                    if (inputSource.getByteStream() != null) {
                        inputSource.getByteStream().close();
                    }
                }
            }
            if (part.element != null) {
                endElem(part.element);
            }
        }
        endElem(this.rootElement);
        this.contentHandler.endDocument();
    }

    /**
     * Private method generating endElement event for the aggregated parts and
     * the root element
     */
    private void endElem(final Element element) throws SAXException {
        final String qname = (element.prefix.equals("")) ? element.name : element.prefix + ':' + element.name;
        this.contentHandler.endElement(element.namespace, element.name, qname);
        if (!element.namespace.equals("")) {
            this.contentHandler.endPrefixMapping(element.prefix);
        }
    }

    /**
     * Private method generating startElement event for the aggregated parts and
     * the root element
     */
    private void startElem(final Element element) throws SAXException {
        final String qname = (element.prefix.equals("")) ? element.name : element.prefix + ':' + element.name;
        if (!element.namespace.equals("")) {
            this.contentHandler.startPrefixMapping(element.prefix, element.namespace);
        }
        this.contentHandler.startElement(element.namespace, element.name, qname, XMLUtils.EMPTY_ATTRIBUTES);
    }
}
