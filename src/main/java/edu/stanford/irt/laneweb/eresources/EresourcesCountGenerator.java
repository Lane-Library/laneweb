/*
 * Created on Jan 10, 2006 To change the template for this generated file go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class EresourcesCountGenerator extends AbstractGenerator {

    private static final String SQL_NS = "http://apache.org/cocoon/SQL/2.0";

    private CollectionManager collectionManager;

    private String query;

    private Set<String> subsets = Collections.emptySet();

    private Set<String> types = Collections.emptySet();

    public void generate() throws SAXException {
        Map<String, Integer> results = this.collectionManager.searchCount(this.types, this.subsets, this.query);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", SQL_NS);
        XMLUtils.startElement(this.xmlConsumer, SQL_NS, "rowset");
        for (Entry<String, Integer> entry : results.entrySet()) {
            String hits = entry.getValue().toString();
            XMLUtils.startElement(this.xmlConsumer, SQL_NS, "row");
            XMLUtils.startElement(this.xmlConsumer, SQL_NS, "genre");
            XMLUtils.data(this.xmlConsumer, entry.getKey());
            XMLUtils.endElement(this.xmlConsumer, SQL_NS, "genre");
            XMLUtils.startElement(this.xmlConsumer, SQL_NS, "hits");
            XMLUtils.data(this.xmlConsumer, hits);
            XMLUtils.endElement(this.xmlConsumer, SQL_NS, "hits");
            XMLUtils.endElement(this.xmlConsumer, SQL_NS, "row");
        }
        XMLUtils.endElement(this.xmlConsumer, SQL_NS, "rowset");
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setSubsets(final Set<String> subsets) {
        if (null == subsets) {
            throw new IllegalArgumentException("null subsets");
        }
        this.subsets = subsets;
    }

    public void setTypes(final Set<String> types) {
        if (null == types) {
            throw new IllegalArgumentException("null types");
        }
        this.types = types;
    }

    protected void initialize() {
        this.query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }
}
