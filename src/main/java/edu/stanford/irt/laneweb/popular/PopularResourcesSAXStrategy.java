package edu.stanford.irt.laneweb.popular;

import java.util.List;
import java.util.Map;

import org.glassfish.jaxb.runtime.util.AttributesImpl;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PopularResourcesSAXStrategy extends AbstractXHTMLSAXStrategy<List<Map<String, String>>> {

    @Override
    public void toSAX(final List<Map<String, String>> list, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            for (Map<String, String> row : list) {
                String id = row.get("id");
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "data-lsid", "data-lsid", "CDATA", id);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "li", atts);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "li");
            }
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
