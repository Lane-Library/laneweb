package edu.stanford.irt.laneweb.cocoon;

import org.xml.sax.SAXException;

/**
 * A Transformer that removes all start/end prefix mapping but the first xhtml
 * empty prefix (&gt;html xmlns="http://www.w3.org/1999/xhtml"&gt;) and its
 * corresponding end prefix mapping, removing all xmlns attributes.
 */
public class NamespaceFilter extends AbstractTransformer {

    /** the number of xhtml ns mappings withoug end mapping */
    private int xhtmlPrefixLevel = 0;

    /**
     * Only reports the end mapping to the next consumer if it is the last end
     * xhtml ns mapping.
     */
    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        if ("".equals(prefix)) {
            --this.xhtmlPrefixLevel;
            if (this.xhtmlPrefixLevel == 0) {
                this.xmlConsumer.endPrefixMapping(prefix);
            }
        }
    }

    /**
     * Only reports the start mapping to the next consumer if it is the first
     * xhtml ns mapping.
     */
    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if ("".equals(prefix) && "http://www.w3.org/1999/xhtml".equals(uri)) {
            if (this.xhtmlPrefixLevel == 0) {
                this.xmlConsumer.startPrefixMapping(prefix, uri);
            }
            this.xhtmlPrefixLevel++;
        }
    }
}
