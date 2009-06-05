package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.user.User;

public class CMELinkTransformer extends AbstractTransformer {

    private static final String EMPTY_STRING = "";

    private static final String HREF = "href";

    private static final String UTD_CME_URL = "http://www.uptodate.com/online/content/search.do?";
    
    private static final String UTD_CME_ARGS = "unid=?&srcsys=epicXXX&eiv=2.1.0";

    // TODO: once more vendors, will want to move UTD strings out to
    // CME_HOSTS<host, cme_args> object
    private static final String[] UTD_HOSTS = { "www.utdol.com", "www.uptodate.com" };

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
    public void startElement(final String uri, final String localName, final String name, final Attributes atts) throws SAXException {
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
            }
            if ("url".equals(localName)) {
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
