package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
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

//    private Collection<String> engines;

    private MetaSearchManager metaSearchManager;

    private String searchTerms;

    private XMLConsumer xmlConsumer;

    public void generate() throws SAXException {
        List<String> engines = new ArrayList<String>();
        engines.add("pubmed");
        engines.add("wiley_cochranelibrary");
        engines.add("acpjc");
        engines.add("uptodate");
        engines.add("dare");
        engines.add("bmj_clinical_evidence");
        engines.add("emedicine");
        engines.add("pubmed_guidelines");
        engines.add("pubmed_treatment_systematic_reviews");
        engines.add("pubmed_prognosis_systematic_reviews");
        engines.add("pubmed_diagnosis_systematic_reviews");
        engines.add("pubmed_harm_systematic_reviews");
        
        engines.add("pubmed_recent_reviews");
        engines.add("statref_acppier");
        engines.add("pubmed_treatment_clinical_trials");
        engines.add("pubmed_treatment_focused");
        engines.add("pubmed_diagnosis_clinical_trials");
        engines.add("pubmed_diagnosis_focused");
        engines.add("pubmed_prognosis_clinical_trials");
        engines.add("pubmed_prognosis_focused");
        engines.add("pubmed_harm_clinical_trials");
        engines.add("pubmed_harm_focused");
        engines.add("pubmed_etiology_focused");
        engines.add("pubmed_etiology_expanded");
        engines.add("pubmed_epidemiology_focused");
        engines.add("pubmed_epidemiology_expanded");
        Query query = new SimpleQuery(this.searchTerms);
        Result result = this.metaSearchManager.search(query, 20000, engines, true);
        SAXable xml = new SAXResult(result);
        xml.toSAX(this.xmlConsumer);
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }
    
    public void setEngines(List<String> engines) {
//        this.engines = engines;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.searchTerms = par.getParameter("search-terms", null);
    }
}
