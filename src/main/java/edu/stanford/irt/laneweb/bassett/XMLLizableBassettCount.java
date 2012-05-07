package edu.stanford.irt.laneweb.bassett;

import java.util.Map;
import java.util.Set;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.laneweb.util.XMLizable;

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

    public void toSAX(final XMLConsumer xmlConsumer) throws SAXException {
        xmlConsumer.startPrefixMapping("", NAMESPACE);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, COUNT);
        handleRegionCounter(xmlConsumer);
        XMLUtils.endElement(xmlConsumer, NAMESPACE, COUNT);
        xmlConsumer.endPrefixMapping("");
    }

    private void handleRegionCounter(final XMLConsumer xmlConsumer) throws SAXException {
        AttributesImpl attributes;
        Set<String> keys = this.regionMap.keySet();
        int i = 0;
        for (String key : keys) {
            Integer count = this.regionMap.get(key);
            if ((key.indexOf("--") == -1) && (0 != i)) {
                XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
            }
            i++;
            if (key.indexOf("--") == -1) {
                attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", key.toLowerCase());
                attributes.addAttribute(NAMESPACE, TOTAL, TOTAL, "CDATA", count.toString());
                XMLUtils.startElement(xmlConsumer, NAMESPACE, REGION, attributes);
            } else {
                attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", key.split("--")[1].toLowerCase());
                XMLUtils.startElement(xmlConsumer, NAMESPACE, SUB_REGION, attributes);
                XMLUtils.data(xmlConsumer, count.toString());
                XMLUtils.endElement(xmlConsumer, NAMESPACE, SUB_REGION);
            }
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
    }
}
