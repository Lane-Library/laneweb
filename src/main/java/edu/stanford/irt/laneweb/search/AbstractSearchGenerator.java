package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

public abstract class AbstractSearchGenerator implements Generator {

    private XMLConsumer xmlConsumer;

    protected MetaSearchManager metaSearchManager;
    
    protected String query;

    public void generate() throws SAXException {
        Result result = doSearch();
        SAXable xml = new SAXResult(result);
        synchronized (result) {
            xml.toSAX(this.xmlConsumer);
        }
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = par.getParameter("query", null);
        Assert.notNull(this.query, "query cannot be null"); 
    }
    protected abstract Result doSearch();
}
