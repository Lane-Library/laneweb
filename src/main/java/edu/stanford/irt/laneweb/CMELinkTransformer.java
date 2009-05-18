package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.user.User;

public class CMELinkTransformer extends AbstractTransformer {

  //TODO: once more vendors, will want to move UTD strings out to CME_HOSTS<host, cme_args> object
  private static final String[] UTD_HOSTS = {"www.utdol.com","www.uptodate.com"};

  private static final String UTD_CME_STRING = "unid=?&srcsys=epicXXX&eiv=2.1.0";

  private static final String EMPTY_STRING = "";

  private static final String HREF = "href";

  private static final String QUESTION = "?";

  private String emrid;
  
  private boolean isSearchUrlElement;

  @SuppressWarnings("unchecked")
  public void setup(final SourceResolver resolver, final Map objectModel,
      final String src, final Parameters params) {
    this.emrid = params.getParameter(User.EMRID, EMPTY_STRING);
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    if (!EMPTY_STRING.equals(this.emrid) && this.isSearchUrlElement) {
      String value = new String(ch, start, length);
      if (isCMEHost(value)){
        value = createCMELink(value);
        this.xmlConsumer.characters(value.toCharArray(), start, value.length());
        return;
      }
    }
    this.xmlConsumer.characters(ch, start, length);
  }
  
  @Override
  public void startElement(final String uri, final String localName,
      final String name, final Attributes atts) throws SAXException {
    this.isSearchUrlElement = false;
    if (!EMPTY_STRING.equals(this.emrid)) {
      if ("a".equals(localName)) {
        String link = atts.getValue(HREF);
        if (null != link && link.indexOf("http") == 0 && isCMEHost(link)) {
          AttributesImpl newAttributes = new AttributesImpl(atts);
          newAttributes.setValue(newAttributes.getIndex(HREF),
              createCMELink(link));
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
    sb.append(link);
    if (!link.contains(QUESTION)) {
      sb.append(QUESTION);
    } else {
      sb.append("&");
    }
    sb.append(UTD_CME_STRING.replaceFirst("\\?", this.emrid));
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
