package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.sitemap.DefaultContentAggregator;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ContentAggregator extends DefaultContentAggregator {
    
    private SAXParser saxParser;

    public ContentAggregator(final SAXParser saxParser) {
        this.saxParser = saxParser;
    }
    
    public void generate()
    throws IOException, SAXException, ProcessingException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Generating aggregated content");
        }
        this.contentHandler.startDocument();
        startElem(this.rootElement);

        for (int i = 0; i < this.parts.size(); i++) {
            final Part part = (Part) this.parts.get(i);
//            this.rootElementIndex = part.stripRootElement ? -1 : 0;
            if (part.element != null) {
                this.currentElement = part.element;
                startElem(part.element);
            } else {
                this.currentElement = this.rootElement;
            }
            if (part.source instanceof XMLizable) {
                ((XMLizable)part.source).toSAX(this);
            } else {
                InputStream input = null;
                try {
                    input = part.source.getInputStream();
                    this.saxParser.parse(new InputSource(input), this, this);
                } finally {
                    input.close();
                }
            }
            if (part.element != null) {
                endElem(part.element);
            }
        }

        endElem(this.rootElement);
        this.contentHandler.endDocument();
            
        getLogger().debug("Finished aggregating content");
    }

    /**
     * Private method generating startElement event for the aggregated parts
     * and the root element
     */
    private void startElem(Element element)
    throws SAXException {
        final String qname = (element.prefix.equals("")) ? element.name : element.prefix + ':' + element.name;
        if (!element.namespace.equals("")) {
            this.contentHandler.startPrefixMapping(element.prefix, element.namespace);
        }
        this.contentHandler.startElement(element.namespace, element.name, qname, XMLUtils.EMPTY_ATTRIBUTES);
    }

    /**
     * Private method generating endElement event for the aggregated parts
     * and the root element
     */
    private void endElem(Element element) throws SAXException {
        final String qname = (element.prefix.equals("")) ? element.name : element.prefix + ':' + element.name;
        this.contentHandler.endElement(element.namespace, element.name, qname);
        if (!element.namespace.equals("")) {
            this.contentHandler.endPrefixMapping(element.prefix);
        }
    }
}
