package edu.stanford.irt.laneweb.images;

import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class BassettCountSAXStrategy implements SAXStrategy<Map<String, Map<String, Integer>>>{

    private static final String BASSETT_COUNT = "bassett_count";

    private static final String CDATA = "CDATA";

    private static final String NAME = "name";

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String REGION = "region";

    private static final String SUB_REGION = "sub_region";

   
    @Override
    public void toSAX(final Map<String, Map<String, Integer>> facets, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT_COUNT);
            Set<String> regions = facets.keySet();
            for (String region : regions) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, CDATA, region.toLowerCase());
                XMLUtils.startElement(xmlConsumer, NAMESPACE, REGION, attributes);
                handleSubRegion(xmlConsumer, facets.get(region));
                XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT_COUNT);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void handleSubRegion(final XMLConsumer xmlConsumer, final Map<String, Integer> subRegionMap)
            throws SAXException {
        Set<String> subRegions = subRegionMap.keySet();
        for (String subRegion : subRegions) {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(NAMESPACE, NAME, NAME, CDATA, subRegion);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, SUB_REGION, attributes);
            XMLUtils.data(xmlConsumer, String.valueOf(subRegionMap.get(subRegion)));
            XMLUtils.endElement(xmlConsumer, NAMESPACE, SUB_REGION);
        }
    }
}
