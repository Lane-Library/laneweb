package edu.stanford.irt.laneweb.seminars;

import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SeminarsLinkSAXStrategy implements SAXStrategy<Map<String, String>> {

    private static final String EMPTY_NS = "";

    private static final String LINK = "link";

    private static final String NAMESPACE = "http://lane.stanford.edu/seminars/ns";

    @Override
    public void toSAX(final Map<String, String> map, final XMLConsumer xmlConsumer) {
        AttributesImpl attributes = new AttributesImpl();
        for (Entry<String, String> entry : map.entrySet()) {
            attributes.addAttribute(NAMESPACE, entry.getKey(), entry.getKey(), "CDATA", entry.getValue());
        }
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping(EMPTY_NS, NAMESPACE);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, LINK, attributes);
            XMLUtils.endElement(xmlConsumer, NAMESPACE, LINK);
            xmlConsumer.endPrefixMapping(EMPTY_NS);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }


}
