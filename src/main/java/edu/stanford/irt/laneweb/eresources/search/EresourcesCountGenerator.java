package edu.stanford.irt.laneweb.eresources.search;

import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourcesCountGenerator extends AbstractGenerator {

    private static final String FACETS_NS = "http://lane.stanford.edu/hitcounts/1.0";

    private String query;
    
    private EresourceSearchService searchService;

    public EresourcesCountGenerator(EresourceSearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        String q = this.query == null ? "" : this.query;
        Map<String, Integer> results = this.searchService.searchCount(q);
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", FACETS_NS);
            XMLUtils.startElement(xmlConsumer, FACETS_NS, "hitcounts");
            for (Entry<String, Integer> entry : results.entrySet()) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "name", "name", "CDATA", entry.getKey());
                atts.addAttribute("", "hits", "hits", "CDATA", entry.getValue().toString());
                XMLUtils.startElement(xmlConsumer, FACETS_NS, "facet", atts);
                XMLUtils.endElement(xmlConsumer, FACETS_NS, "facet");
            }
            XMLUtils.endElement(xmlConsumer, FACETS_NS, "hitcounts");
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
