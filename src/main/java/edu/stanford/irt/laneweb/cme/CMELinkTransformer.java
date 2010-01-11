package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.user.User;

/**
 * Transformer to rewrite data when a User.EMRID value is present. Elements to rewrite include: - anchor element href
 * attributes when value is known CME host - url element of metasearch results when value is known CME host - input
 * element value attributes when found value is REPLACEMENT_STRING
 * 
 * @author ryanmax
 */
public class CMELinkTransformer extends AbstractTransformer {

    private static final String EMPTY_STRING = "";

    private static final String HREF = "href";

    private static final String REPLACEMENT_STRING = "{emrid}";

    private static final String UTD_CME_ARGS = "unid=?&srcsys=epic90710&eiv=2.1.0";

    private static final String UTD_CME_URL = "http://www.uptodate.com/online/content/search.do?";

    // TODO: once more vendors, move UTD strings to collection of host objects
    private static final String[] UTD_HOSTS = { "www.utdol.com", "www.uptodate.com" };

    private static final String VALUE = "value";

    private String emrid;

    private boolean isSearchUrlElement;

    @Override
    public void characters(final char ch[], final int start, final int length) throws SAXException {
        if (!EMPTY_STRING.equals(this.emrid) && this.isSearchUrlElement) {
            String value = new String(ch, start, length);
            if (isCMEHost(value)) {
                value = createCMELink(value);
                this.xmlConsumer.characters(value.toCharArray(), start, value.length());
                return;
            }
        }
        this.xmlConsumer.characters(ch, start, length);
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.emrid = params.getParameter(User.EMRID, EMPTY_STRING);
    }

    @Override
    public void startElement(final String uri, final String localName, final String name, final Attributes atts)
            throws SAXException {
        this.isSearchUrlElement = false;
        if (!EMPTY_STRING.equals(this.emrid)) {
            if ("a".equals(localName)) {
                String link = atts.getValue(HREF);
                if (null != link && link.indexOf("http") == 0 && isCMEHost(link)) {
                    AttributesImpl newAttributes = new AttributesImpl(atts);
                    newAttributes.setValue(newAttributes.getIndex(HREF), createCMELink(link));
                    this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                    return;
                }
            } else if ("input".equals(localName)) {
                String value = atts.getValue(VALUE);
                if (null != value && REPLACEMENT_STRING.equals(value)) {
                    AttributesImpl newAttributes = new AttributesImpl(atts);
                    newAttributes.setValue(newAttributes.getIndex(VALUE), this.emrid);
                    this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                    return;
                }
            } else if ("url".equals(localName)) {
                this.isSearchUrlElement = true;
            }
        }
        this.xmlConsumer.startElement(uri, localName, name, atts);
    }

    private String createCMELink(final String link) {
        StringBuffer sb = new StringBuffer();
        if (link.contains("?")) {
            sb.append(link).append("&").append(UTD_CME_ARGS.replaceFirst("\\?", this.emrid));
        } else if (link.endsWith("/") || link.endsWith("online")) {
            sb.append(UTD_CME_URL).append(UTD_CME_ARGS.replaceFirst("\\?", this.emrid));
        } else {
            sb.append(link);
        }
        return sb.toString();
    }

    private boolean isCMEHost(final String link) {
        for (String host : UTD_HOSTS) {
            if (link.contains(host)) {
                return true;
            }
        }
        return false;
    }
}
