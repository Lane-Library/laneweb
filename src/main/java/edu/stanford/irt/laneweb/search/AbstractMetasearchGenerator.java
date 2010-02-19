package edu.stanford.irt.laneweb.search;

import org.xml.sax.SAXException;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

public abstract class AbstractMetasearchGenerator extends AbstractSearchGenerator {

    protected MetaSearchManager metaSearchManager;

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

    protected abstract Result doSearch();
}
