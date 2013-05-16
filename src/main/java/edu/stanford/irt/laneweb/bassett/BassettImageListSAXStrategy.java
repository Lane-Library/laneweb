package edu.stanford.irt.laneweb.bassett;

import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class BassettImageListSAXStrategy implements SAXStrategy<List<BassettImage>> {

    private static final String BASSETT = "bassett";

    private static final String BASSETT_IMAGE = "bassett_image";

    private static final String BASSETT_NUMBER = "bassett_number";

    private static final String BASSETTS = "bassetts";

    private static final String DESCRIPTION = "description";

    private static final String DIAGRAM = "diagram_image";

    private static final String LEGEND = "legend";

    private static final String LEGEND_IMAGE = "legend_image";

    private static final String NAME = "name";

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String REGION = "region";

    private static final String REGIONS = "regions";

    private static final String SUB_REGION = "sub_region";

    private static final String TITLE = "title";

    public void toSAX(final List<BassettImage> bassetts, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETTS);
            if (bassetts != null) {
                for (BassettImage eresource : bassetts) {
                    handleEresource(xmlConsumer, eresource);
                }
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETTS);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void handleEresource(final XMLConsumer xmlConsumer, final BassettImage bassett) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(NAMESPACE, BASSETT_NUMBER, BASSETT_NUMBER, "CDATA", bassett.getBassettNumber());
        XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT, attributes);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, TITLE);
        XMLUtils.data(xmlConsumer, bassett.getTitle());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, TITLE);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT_IMAGE);
        XMLUtils.data(xmlConsumer, bassett.getImage());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT_IMAGE);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, DIAGRAM);
        XMLUtils.data(xmlConsumer, bassett.getDiagram());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, DIAGRAM);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, LEGEND_IMAGE);
        XMLUtils.data(xmlConsumer, bassett.getLatinLegend());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, LEGEND_IMAGE);
        if (null != bassett.getEngishLegend()) {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, LEGEND);
            XMLUtils.data(xmlConsumer, bassett.getEngishLegend());
            XMLUtils.endElement(xmlConsumer, NAMESPACE, LEGEND);
        }
        if (null != bassett.getDescription()) {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, DESCRIPTION);
            XMLUtils.data(xmlConsumer, bassett.getDescription());
            XMLUtils.endElement(xmlConsumer, NAMESPACE, DESCRIPTION);
        }
        handleRegion(xmlConsumer, bassett.getRegions());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT);
    }

    private void handleRegion(final XMLConsumer xmlConsumer, final List<String> regions) throws SAXException {
        String currentRegion = null;
        XMLUtils.startElement(xmlConsumer, NAMESPACE, REGIONS);
        for (int i = 0; i < regions.size(); i++) {
            String region = regions.get(i);
            String[] splittedRegion = region.split("--");
            if (!splittedRegion[0].equals(currentRegion)) {
                if (i != 0) {
                    XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
                }
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, "CDATA", splittedRegion[0]);
                XMLUtils.startElement(xmlConsumer, NAMESPACE, REGION, attributes);
                currentRegion = splittedRegion[0];
            }
            if (splittedRegion.length > 1) {
                XMLUtils.startElement(xmlConsumer, NAMESPACE, SUB_REGION);
                XMLUtils.data(xmlConsumer, splittedRegion[1]);
                XMLUtils.endElement(xmlConsumer, NAMESPACE, SUB_REGION);
            }
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
        XMLUtils.endElement(xmlConsumer, NAMESPACE, REGIONS);
    }
}
