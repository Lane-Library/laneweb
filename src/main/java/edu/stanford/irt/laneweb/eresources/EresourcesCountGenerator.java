/*
 * Created on Jan 10, 2006 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;

public class EresourcesCountGenerator implements Generator {

    private static final String QUERY = "q";

    private static final String SQL_NS = "http://apache.org/cocoon/SQL/2.0";

    private Set<String> types = Collections.emptySet();

    private Set<String> subsets = Collections.emptySet();

    private CollectionManager collectionManager;

    private String query;

    private XMLConsumer xmlConsumer;

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        if (null != query) {
            this.query = query.trim();
            if (this.query.length() == 0) {
                this.query = null;
            }
        } else {
            throw new RuntimeException("null query");
        }
    }

    public void generate() throws SAXException {
        Map<String, Integer> result = this.collectionManager.searchCount(this.types, this.subsets, this.query);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", SQL_NS);
        XMLUtils.startElement(this.xmlConsumer, SQL_NS, "rowset");
        for (String genre : result.keySet()) {
            String hits = result.get(genre).toString();
            XMLUtils.startElement(this.xmlConsumer, SQL_NS, "row");
            XMLUtils.startElement(this.xmlConsumer, SQL_NS, "genre");
            XMLUtils.data(this.xmlConsumer, genre);
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

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }

    public void setTypes(final Set<String> types) {
        if (null == types) {
            throw new IllegalArgumentException("null types");
        }
        this.types = types;
    }

    public void setSubsets(final Set<String> subsets) {
        if (null == subsets) {
            throw new IllegalArgumentException("null subsets");
        }
        this.subsets = subsets;
    }
}
