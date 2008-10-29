package edu.stanford.irt.laneweb.eresources;

import java.util.Map;
import java.util.Set;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLLizableBassettCount implements XMLizable {

    private Map<String, Integer> regionMap;

    private String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String COUNT = "bassett_count";

    private static final String REGION = "region";

    private static final String SUB_REGION = "sub_region";

    private static final String NAME = "name";

    private static final String TOTAL = "total";

    public XMLLizableBassettCount(final Map<String, Integer> regions) {
	this.regionMap = regions;
    }

    public void toSAX(final ContentHandler consumer) throws SAXException {
	consumer.startPrefixMapping("", this.NAMESPACE);
	XMLUtils.startElement(consumer, this.NAMESPACE, COUNT);
	handleRegionCounter(consumer);
	XMLUtils.endElement(consumer, this.NAMESPACE, COUNT);
	consumer.endPrefixMapping("");
    }

    private void handleRegionCounter(final ContentHandler consumer) throws SAXException {
	AttributesImpl attributes;
	Set<String> keys = this.regionMap.keySet();
	int i = 0;
	for (String key : keys) {
	    Integer count = this.regionMap.get(key);
	    if (key.indexOf("--") == -1 && 0 != i)
	        XMLUtils.endElement(consumer, this.NAMESPACE, REGION);
	    i++;
	    if (key.indexOf("--") == -1) {
		attributes = new AttributesImpl();
		attributes.addAttribute(this.NAMESPACE, NAME, NAME, "CDATA", key.toLowerCase());
		attributes.addAttribute(this.NAMESPACE, TOTAL, TOTAL, "CDATA", count.toString());
		XMLUtils.startElement(consumer, this.NAMESPACE, REGION, attributes);
	    } else {
		attributes = new AttributesImpl();
		attributes.addAttribute(this.NAMESPACE, NAME, NAME, "CDATA", key.split("--")[1].toLowerCase());
		XMLUtils.startElement(consumer, this.NAMESPACE, SUB_REGION, attributes);
		XMLUtils.data(consumer, count.toString());
		XMLUtils.endElement(consumer, this.NAMESPACE, SUB_REGION);
	    }
	}
	XMLUtils.endElement(consumer, this.NAMESPACE, REGION);
    }

}
