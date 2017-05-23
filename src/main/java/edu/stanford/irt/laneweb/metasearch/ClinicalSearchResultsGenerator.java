package edu.stanford.irt.laneweb.metasearch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ClinicalSearchResultsGenerator extends AbstractMetasearchGenerator<ClinicalSearchResults> {

    private static final Result EMPTY_RESULT = Result.newResultBuilder().id("").query(new SimpleQuery("")).build();

    private static final long MAX_WAIT_TIME = 20_000L;

    private List<String> engines;

    private List<String> facets;

    private ClinicalSearchResultsFactory factory;

    private int page;

    public ClinicalSearchResultsGenerator(final MetaSearchManager metaSearchManager,
            final SAXStrategy<ClinicalSearchResults> saxStrategy, final List<String> engines,
            final ClinicalSearchResultsFactory factory) {
        super(metaSearchManager, saxStrategy);
        this.engines = engines;
        this.factory = factory;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String f = ModelUtil.getString(model, Model.FACET, null);
        this.facets = f == null ? Collections.emptyList() : Arrays.asList(f.split(","));
        String p = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = Integer.parseInt(p) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
    }

    @Override
    protected ClinicalSearchResults doSearch(final String query) {
        return this.factory.createResults(search(new SimpleQuery(query), this.engines, MAX_WAIT_TIME), query,
                this.facets, this.page);
    }

    @Override
    protected ClinicalSearchResults getEmptyResult() {
        return this.factory.createResults(EMPTY_RESULT, "", this.facets, this.page);
    }
}
