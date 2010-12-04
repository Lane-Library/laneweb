package edu.stanford.irt.laneweb.bassett;

import java.util.Map;
import java.util.Set;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLLizableBassettCount implements XMLizable {

    private static final String COUNT = "bassett_count";

    private static final String NAME = "name";

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String REGION = "region";

    private static final String SUB_REGION = "sub_region";

    private static final String TOTAL = "total";

    private Map<String, Integer> regionMap;

    public XMLLizableBassettCount(final Map<String, Integer> regions) {
        this.regionMap = regions;
    }

    public void toSAX(final ContentHandler consumer) throws SAXException {
        consumer.startPrefixMapping("", NAMESPACE);
        XMLUtils.startElement(consumer, NAMESPACE, COUNT);
        handleRegionCounter(consumer);
        XMLUtils.endElement(consumer, NAMESPACE, COUNT);
        consumer.endPrefixMapping("");
    }

    private void handleRegionCounter(final ContentHandler consumer) throws SAXException {
        AttributesImpl attributes;
        Set<String> keys = this.regionMap.keySet();
        int i = 0;
        for (String key : keys) {
            Integer count = this.regionMap.get(key);
            if ((key.indexOf("--") == -1) && (0 != i)) {
                XMLUtils.endElement(consumer, NAMESPACE, REGION);
            }
            i++;
            if (key.indexOf("--") == -1) {
                attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", key.toLowerCase());
                attributes.addAttribute(NAMESPACE, TOTAL, TOTAL, "CDATA", count.toString());
                XMLUtils.startElement(consumer, NAMESPACE, REGION, attributes);
            } else {
                attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", key.split("--")[1].toLowerCase());
                XMLUtils.startElement(consumer, NAMESPACE, SUB_REGION, attributes);
                XMLUtils.data(consumer, count.toString());
                XMLUtils.endElement(consumer, NAMESPACE, SUB_REGION);
            }
        }
        XMLUtils.endElement(consumer, NAMESPACE, REGION);
    }
}
