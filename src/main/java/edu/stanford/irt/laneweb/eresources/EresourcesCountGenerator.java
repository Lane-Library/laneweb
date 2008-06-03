/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.CollectionManager;

public class EresourcesCountGenerator extends ServiceableGenerator {
    
    private static final String KEYWORDS = "keywords";

    private static final String QUERY = "q";
    
    private static final String SQL_NS = "http://apache.org/cocoon/SQL/2.0";
    
    private static final String[] TYPE_ARRAY =
    {"ej","database","video","book","cc","lanesite"};
    
    private static final String[] SUBSET_ARRAY =
    {"biotools"};
    
    private static Set<String> TYPES;
    
    private static Set<String> SUBSETS;
    
    static {
        TYPES = new HashSet<String>();
        for (String type : TYPE_ARRAY) {
            TYPES.add(type);
        }
        SUBSETS = new HashSet<String>();
        for (String subset : SUBSET_ARRAY) {
            SUBSETS.add(subset);
        }
    }

    private CollectionManager collectionManager;
    
    private String query;

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        if (this.query == null) {
            this.query = request.getParameter(KEYWORDS);
        }
        if (null != query) {
            this.query = query.trim();
            if (this.query.length() == 0) {
                this.query = null;
            }

        }
    }

    public void generate() throws SAXException {
        Map<String, Integer> result = this.collectionManager.searchCount(TYPES, SUBSETS, this.query);
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

    @Override
    public void recycle() {
        this.query = null;
    }

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        this.collectionManager = (CollectionManager) manager.lookup(CollectionManager.class.getName());
    }

    @Override
    public void dispose() {
        this.manager.release(this.collectionManager);
        super.dispose();
    }
}
