package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.DefaultModelAware;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

public abstract class AbstractSearchGenerator extends DefaultModelAware implements Generator {

    protected XMLConsumer xmlConsumer;

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
        this.query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }

    protected abstract Result doSearch();
}
