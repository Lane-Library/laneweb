package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

/**
 * @author ceyates
 */
public class ContentSearchGenerator implements Generator {

    private Collection<String> engines;

    private MetaSearchManager metaSearchManager;

    private String searchTerms;

    private XMLConsumer xmlConsumer;

    public void generate() throws SAXException {
        Query query = new SimpleQuery(this.searchTerms);
        Result result = this.metaSearchManager.search(query, 20000, this.engines, true);
        SAXable xml = new SAXResult(result);
        xml.toSAX(this.xmlConsumer);
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setEngines(final List<String> engines) {
        this.engines = engines;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.searchTerms = par.getParameter("search-terms", null);
    }
}
