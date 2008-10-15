/*
 * Created on Jan 10, 2006 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;

public class EresourcesCountGenerator extends ServiceableGenerator implements Configurable, Initializable {

    private static final String QUERY = "q";

    private static final String SQL_NS = "http://apache.org/cocoon/SQL/2.0";

    private Set<String> types;

    private Set<String> subsets;

    private CollectionManager collectionManager;

    private String query;

    private String collection;

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        if (null != query) {
            this.query = query.trim();
            if (this.query.length() == 0) {
                this.query = null;
            }

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

    @Override
    public void recycle() {
        this.query = null;
    }

    @Override
    public void dispose() {
        this.manager.release(this.collectionManager);
        super.dispose();
    }

    public void configure(final Configuration conf) throws ConfigurationException {
        this.types = new HashSet<String>();
        Configuration[] typeConf = conf.getChildren("type");
        for (Configuration element : typeConf) {
            this.types.add(element.getValue());
        }
        this.subsets = new HashSet<String>();
        Configuration[] subsetConf = conf.getChildren("subset");
        for (Configuration element : subsetConf) {
            this.subsets.add(element.getValue());
        }
        this.collection = conf.getChild("collection").getValue();
    }

    public void initialize() throws ServiceException {
        setCollectionManager((CollectionManager) this.manager.lookup(CollectionManager.class.getName() + "/" + this.collection));
    }
}
