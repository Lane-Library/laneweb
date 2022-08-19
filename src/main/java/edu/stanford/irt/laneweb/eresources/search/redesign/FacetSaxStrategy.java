package edu.stanford.irt.laneweb.eresources.search.redesign;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class FacetSaxStrategy implements SAXStrategy<Map<String, Collection<FacetFieldEntry>>> {

    private static final String CDATA = "CDATA";

    private Collection<String> selectedFacets;
    
    public FacetSaxStrategy(Collection<String> facetFields) {
       this.selectedFacets = facetFields;
    }

    public void toSAX(final Map<String, Collection<FacetFieldEntry>> facetPages, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, "facetResult");
            for (String facetName : this.selectedFacets ) {
                Collection<FacetFieldEntry> facets = facetPages.get(facetName);
                for (FacetFieldEntry facet : facets) {
                    saxFacet(facet, xmlConsumer);
                }
            }
            XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, "facetResult");
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void saxFacet(FacetFieldEntry facet, final XMLConsumer xmlConsumer) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(Resource.NAMESPACE, "key", "key", CDATA, facet.getKey().getName());
        attributes.addAttribute(Resource.NAMESPACE, "name", "name", CDATA, facet.getValue());
        attributes.addAttribute(Resource.NAMESPACE, "value", "value", CDATA, String.valueOf(facet.getValueCount()));
        XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, "facet", attributes);
        XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, "facet");
    }
 
}
