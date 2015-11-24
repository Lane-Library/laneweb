package edu.stanford.irt.laneweb.bassett;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.BassettImage;

public class BassettCountSAXStrategy implements SAXStrategy<FacetPage<BassettImage>> {

    private static final String BASSETT_COUNT = "bassett_count";

    private static final String CDATA = "CDATA";

    private static final String NAME = "name";

    private static final String NAMESPACE = "http://lane.stanford.edu/bassett/ns";

    private static final String REGION = "region";

    private static final String SUB_REGION = "sub_region";

    private static final String TOTAL = "total";

    @Override
    public void toSAX(final FacetPage<BassettImage> facetPage, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            XMLUtils.startElement(xmlConsumer, NAMESPACE, BASSETT_COUNT);
            Page<FacetFieldEntry> regions =  facetPage.getFacetResultPage(REGION);
            Page<FacetFieldEntry> subRegions =  facetPage.getFacetResultPage(SUB_REGION);
            for (FacetFieldEntry entry : regions) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute(NAMESPACE, NAME, NAME, CDATA, entry.getValue().replace("_", " "));
                attributes.addAttribute(NAMESPACE, TOTAL, TOTAL, CDATA, String.valueOf(entry.getValueCount()));
                XMLUtils.startElement(xmlConsumer, NAMESPACE, REGION, attributes);
                handleSubRegion(xmlConsumer, entry.getValue(), subRegions);
                XMLUtils.endElement(xmlConsumer, NAMESPACE, REGION);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, BASSETT_COUNT);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }


    private void handleSubRegion(final XMLConsumer xmlConsumer, final String region, final Page<FacetFieldEntry> subRegions) throws SAXException 
    {
        for (FacetFieldEntry entry : subRegions) {
            String subRegion = entry.getValue();
             if(subRegion.startsWith(region+"_"+SUB_REGION+"_")){
                AttributesImpl attributes = new AttributesImpl();
                subRegion = subRegion.substring(subRegion.indexOf(SUB_REGION)+ 11);
                attributes.addAttribute(NAMESPACE, NAME, NAME, CDATA, subRegion.replace("_", " "));
                XMLUtils.startElement(xmlConsumer, NAMESPACE, SUB_REGION, attributes);
                XMLUtils.data(xmlConsumer, String.valueOf( entry.getValueCount()));
                XMLUtils.endElement(xmlConsumer, NAMESPACE, SUB_REGION);
            }
        }
    }
}
