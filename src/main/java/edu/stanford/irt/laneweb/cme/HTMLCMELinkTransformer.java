package edu.stanford.irt.laneweb.cme;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Transformer to rewrite data when a LDAPData.EMRID value is present. Elements
 * to rewrite include: - anchor element href attributes when value is known CME
 * host - input element value attributes when found value is REPLACEMENT_STRING
 * 
 * @author ryanmax
 */
public class HTMLCMELinkTransformer extends AbstractCMELinkTransformer {

    private static final String ANCHOR = "a";

    private static final String HREF = "href";

    private static final String INPUT = "input";

    private static final String REPLACEMENT_STRING = "{emrid}";

    private static final String VALUE = "value";

    @Override
    public void startElement(final String uri, final String localName, final String name, final Attributes atts)
            throws SAXException {
        if (null != this.emrid) {
            if (ANCHOR.equals(localName)) {
                String link = atts.getValue(HREF);
                if (null != link && link.indexOf("http") == 0 && isCMEHost(link)) {
                    AttributesImpl newAttributes = new AttributesImpl(atts);
                    newAttributes.setValue(newAttributes.getIndex(HREF), createCMELink(link));
                    this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                    return;
                }
            } else if (INPUT.equals(localName)) {
                String value = atts.getValue(VALUE);
                if (null != value && REPLACEMENT_STRING.equals(value)) {
                    AttributesImpl newAttributes = new AttributesImpl(atts);
                    newAttributes.setValue(newAttributes.getIndex(VALUE), this.emrid);
                    this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                    return;
                }
            }
        }
        this.xmlConsumer.startElement(uri, localName, name, atts);
    }
}
