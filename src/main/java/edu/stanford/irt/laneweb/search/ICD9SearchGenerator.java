package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.lane.icd9.ICD9Translator;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

/**
 * @author ceyates
 */
public class ICD9SearchGenerator implements Generator {

    private long defaultTimeout;

    private ICD9Translator icd9Translator;

    private MetaSearchManager metaSearchManager;

    private String q;

    private XMLConsumer xmlConsumer;

    public void generate() throws SAXException {
        Result result = null;
        try {
            String translatedQuery = this.icd9Translator.getLongName(this.q);
            SimpleQuery query = new SimpleQuery(translatedQuery);
            result = this.metaSearchManager.search(query, this.defaultTimeout, null, false);
        } catch (Exception e) {
            result = new DefaultResult("failed");
            result.setStatus(SearchStatus.FAILED);
            result.setException(e);
        }
        SAXable xml = new SAXResult(result);
        synchronized (result) {
            xml.toSAX(this.xmlConsumer);
        }
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setICD9Translator(final ICD9Translator icd9Translator) {
        this.icd9Translator = icd9Translator;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.q = params.getParameter("q", null);
    }
}
