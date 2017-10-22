package edu.stanford.irt.laneweb.cme;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.NOOPXMLConsumer;

/**
 * Transformer to rewrite data when a Model.EMRID or Model.AUTH (hashed userid) value is present. Elements to rewrite
 * include: anchor element href attributes when value is known CME host, input element value attributes when found value
 * is EMRID_REPLACEMENT_STRING
 *
 * @author ryanmax
 */
public class HTMLCMELinkTransformer extends AbstractCMELinkTransformer {

    private static final String ANCHOR = "a";

    private static final String EMRID_REPLACEMENT_STRING = "{emrid}";

    private static final String HREF = "href";

    private static final String INPUT = "input";

    private static final String VALUE = "value";

    private XMLConsumer xmlConsumer;

    public HTMLCMELinkTransformer() {
        this.xmlConsumer = NOOPXMLConsumer.INSTANCE;
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String localName, final String name, final Attributes atts)
            throws SAXException {
        if (ANCHOR.equals(localName)) {
            String link = atts.getValue(HREF);
            if (null != link && link.indexOf("http") == 0 && isCMEHost(link)) {
                AttributesImpl newAttributes = new AttributesImpl(atts);
                StringBuilder url = new StringBuilder();
                url.append(getBasePath());
                url.append(CME_REDIRECT);
                url.append(link);
                newAttributes.setValue(newAttributes.getIndex(HREF), url.toString());
                this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                return;
            }
        } else if (INPUT.equals(localName)) {
            String value = atts.getValue(VALUE);
            String emrid = getEmrid();
            if (null != emrid && EMRID_REPLACEMENT_STRING.equals(value)) {
                AttributesImpl newAttributes = new AttributesImpl(atts);
                newAttributes.setValue(newAttributes.getIndex(VALUE), emrid);
                this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                return;
            }
        }
        this.xmlConsumer.startElement(uri, localName, name, atts);
    }
}
