package edu.stanford.irt.laneweb.search;

import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

public abstract class AbstractSearchGenerator extends AbstractGenerator {

    protected MetaSearchManager metaSearchManager;

    protected String query;

    public void generate() throws SAXException {
        Result result = doSearch();
        SAXable xml = new SAXResult(result);
        synchronized (result) {
            xml.toSAX(this.xmlConsumer);
        }
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    public void initialize() {
        this.query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }

    protected abstract Result doSearch();
}
