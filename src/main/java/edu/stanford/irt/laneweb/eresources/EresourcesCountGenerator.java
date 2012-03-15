package edu.stanford.irt.laneweb.eresources;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourcesCountGenerator extends AbstractGenerator {

    private static final String SQL_NS = "http://apache.org/cocoon/SQL/2.0";

    private CollectionManager collectionManager;

    private String query;

    private Set<String> types = Collections.emptySet();

    public void generate() throws SAXException {
        Map<String, Integer> results = this.collectionManager.searchCount(this.types, null, this.query);
        XMLConsumer xmlConsumer = getXMLConsumer();
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
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setTypes(final Set<String> types) {
        if (null == types) {
            throw new IllegalArgumentException("null types");
        }
        this.types = types;
    }

    @Override
    protected void initialize() {
        this.query = ModelUtil.getString(getModel(), Model.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }
}
