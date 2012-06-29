package edu.stanford.irt.laneweb.bassett;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLizingStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class BassettCountXMLizingStrategy implements XMLizingStrategy<Map<String, Integer>> {

    private static final String BASSETT_COUNT = "bassett_count";

    private static final String NAME = "name";

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String REGION = "region";

    private static final String SUB_REGION = "sub_region";

    private static final String TOTAL = "total";

    public void toSAX(final Map<String, Integer> regionMap, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT_COUNT);
            AttributesImpl attributes;
            boolean haveStartRegion = false;
            for (Entry<String, Integer> entry : regionMap.entrySet()) {
                String key = entry.getKey();
                Integer count = entry.getValue();
                int separatorIndex = key.indexOf("--");
                boolean isRegion = separatorIndex == -1;
                if (isRegion) {
                    if (haveStartRegion) {
                        XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
                    }
                    attributes = new AttributesImpl();
                    attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", key.toLowerCase());
                    attributes.addAttribute(NAMESPACE, TOTAL, TOTAL, "CDATA", count.toString());
                    XMLUtils.startElement(xmlConsumer, NAMESPACE, REGION, attributes);
                    haveStartRegion = true;
                } else {
                    String subregion = key.substring(separatorIndex + 2).toLowerCase();
                    attributes = new AttributesImpl();
                    attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", subregion);
                    XMLUtils.startElement(xmlConsumer, NAMESPACE, SUB_REGION, attributes);
                    XMLUtils.data(xmlConsumer, count.toString());
                    XMLUtils.endElement(xmlConsumer, NAMESPACE, SUB_REGION);
                }
            }
            if (haveStartRegion) {
                XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT_COUNT);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
