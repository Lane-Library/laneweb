package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;

public class XMLLizableBassettEresourceList implements XMLizable {

    Collection<Eresource> bassetts;

    Map<String, Map> regionCounter = new HashMap<String, Map>();

    private String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String BASSETTS = "bassetts";
    private static final String BASSETT = "bassett";
    private static final String BASSETT_NUMBER = "bassett_number";
    private static final String BASSETT_IMAGE = "bassett_image";
    private static final String DIAGRAM = "diagram_image";
    private static final String LEGEND_IMAGE = "legend_image";
    private static final String LEGEND = "legend";
    private static final String REGIONS = "regions";
    private static final String REGION = "region";
    private static final String SUB_REGION = "sub_region";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";

    public XMLLizableBassettEresourceList(final Collection<Eresource> bassetts) {
        this.bassetts = bassetts;
    }

    public void toSAX(final ContentHandler consumer) throws SAXException {
        consumer.startPrefixMapping("", this.NAMESPACE);
        XMLUtils.startElement(consumer, this.NAMESPACE, BASSETTS);
        if (this.bassetts != null) {
            for (Eresource eresource : this.bassetts) {
                handleEresource(consumer, eresource);
            }
        }
        XMLUtils.endElement(consumer, this.NAMESPACE, BASSETTS);
        consumer.endPrefixMapping("");

    }

    private void handleEresource(final ContentHandler handler, final Eresource eresource) throws SAXException {
        BassettEresource bassett = (BassettEresource) eresource;

        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(this.NAMESPACE, BASSETT_NUMBER, BASSETT_NUMBER, "CDATA", bassett.getBassettNumber());
        XMLUtils.startElement(handler, this.NAMESPACE, BASSETT, attributes);

        XMLUtils.startElement(handler, this.NAMESPACE, TITLE);
        XMLUtils.data(handler, bassett.getTitle());
        XMLUtils.endElement(handler, this.NAMESPACE, TITLE);

        XMLUtils.startElement(handler, this.NAMESPACE, BASSETT_IMAGE);
        XMLUtils.data(handler, bassett.getImage());
        XMLUtils.endElement(handler, this.NAMESPACE, BASSETT_IMAGE);

        XMLUtils.startElement(handler, this.NAMESPACE, DIAGRAM);
        XMLUtils.data(handler, bassett.getDiagram());
        XMLUtils.endElement(handler, this.NAMESPACE, DIAGRAM);

        XMLUtils.startElement(handler, this.NAMESPACE, LEGEND_IMAGE);
        XMLUtils.data(handler, bassett.getLatinLegend());
        XMLUtils.endElement(handler, this.NAMESPACE, LEGEND_IMAGE);
        if (null != bassett.getEngishLegend()) {
            XMLUtils.startElement(handler, this.NAMESPACE, LEGEND);
            XMLUtils.data(handler, bassett.getEngishLegend());
            XMLUtils.endElement(handler, this.NAMESPACE, LEGEND);
        }
        if (null != bassett.getDescription()) {
            XMLUtils.startElement(handler, this.NAMESPACE, DESCRIPTION);
            XMLUtils.data(handler, bassett.getDescription());
            XMLUtils.endElement(handler, this.NAMESPACE, DESCRIPTION);
        }
        handleRegion(handler, bassett.getRegions());
        XMLUtils.endElement(handler, this.NAMESPACE, BASSETT);
    }

    private void handleRegion(final ContentHandler handler, final List<String> regions) throws SAXException {
        String currentRegion = null;
        XMLUtils.startElement(handler, this.NAMESPACE, REGIONS);
        for (int i = 0; i < regions.size(); i++) {
            String region = regions.get(i);
            String[] splittedRegion = region.split("--");
            if (!splittedRegion[0].equals(currentRegion)) {
                if (i != 0) {
                    XMLUtils.endElement(handler, this.NAMESPACE, REGION);
                }
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(this.NAMESPACE, NAME, NAME, "CDATA", splittedRegion[0]);
                XMLUtils.startElement(handler, this.NAMESPACE, REGION, attributes);
                currentRegion = splittedRegion[0];
            }
            if (splittedRegion.length > 1) {
                XMLUtils.startElement(handler, this.NAMESPACE, SUB_REGION);
                XMLUtils.data(handler, splittedRegion[1]);
                XMLUtils.endElement(handler, this.NAMESPACE, SUB_REGION);
            }
        }
        XMLUtils.endElement(handler, this.NAMESPACE, REGION);
        XMLUtils.endElement(handler, this.NAMESPACE, REGIONS);
    }

}
