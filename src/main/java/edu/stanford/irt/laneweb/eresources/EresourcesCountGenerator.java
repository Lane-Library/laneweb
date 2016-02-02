package edu.stanford.irt.laneweb.eresources;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.solr.SolrService;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourcesCountGenerator extends AbstractGenerator implements ModelAware {

    private static final int MAX_QUERY_LENGTH = 300;

    private static final String SQL_NS = "http://apache.org/cocoon/SQL/2.0";

    private String query;

    private SolrService solrService;

    private Set<String> types;

    public EresourcesCountGenerator(final Set<String> types, final SolrService solrService) {
        this.solrService = solrService;
        this.types = types;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Map<String, Integer> results = null;
        if (this.query == null || this.query.isEmpty() || this.query.length() > MAX_QUERY_LENGTH) {
            results = new HashMap<String, Integer>();
            Integer zero = Integer.valueOf(0);
            for (String type : this.types) {
                results.put(type, zero);
            }
        } else {
            results = this.solrService.searchCount(this.types, this.query);
        }
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", SQL_NS);
            XMLUtils.startElement(xmlConsumer, SQL_NS, "rowset");
            for (Entry<String, Integer> entry : results.entrySet()) {
                String hits = entry.getValue().toString();
                XMLUtils.startElement(xmlConsumer, SQL_NS, "row");
                XMLUtils.startElement(xmlConsumer, SQL_NS, "genre");
                XMLUtils.data(xmlConsumer, entry.getKey());
                XMLUtils.endElement(xmlConsumer, SQL_NS, "genre");
                XMLUtils.startElement(xmlConsumer, SQL_NS, "hits");
                XMLUtils.data(xmlConsumer, hits);
                XMLUtils.endElement(xmlConsumer, SQL_NS, "hits");
                XMLUtils.endElement(xmlConsumer, SQL_NS, "row");
            }
            XMLUtils.endElement(xmlConsumer, SQL_NS, "rowset");
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
