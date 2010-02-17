package edu.stanford.irt.laneweb;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;

public class NamespaceFilter extends AbstractTransformer {

    public void endPrefixMapping(final String prefix) throws SAXException {
        if ("".equals(prefix)) {
            this.xmlConsumer.endPrefixMapping(prefix);
        }
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if ("".equals(prefix) && "http://www.w3.org/1999/xhtml".equals(uri)) {
            this.xmlConsumer.startPrefixMapping(prefix, uri);
        }
    }
}
