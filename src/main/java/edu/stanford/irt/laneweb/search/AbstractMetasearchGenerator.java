package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

public abstract class AbstractMetasearchGenerator extends AbstractSearchGenerator {

    private MetaSearchManager metaSearchManager;

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Result result = doSearch();
        SAXable xml = new SAXResult(result);
        synchronized (result) {
            try {
                xml.toSAX(xmlConsumer);
            } catch (SAXException e) {
                throw new LanewebException(e);
            }
        }
    }

    protected abstract Result doSearch();

    public Result describe(Query query, Collection<String> engines) {
        return this.metaSearchManager.describe(query, engines);
    }

    public Result search(Query query, long arg1, Collection<String> arg2, boolean arg3) {
        return this.metaSearchManager.search(query, arg1, arg2, arg3);
    }
}
