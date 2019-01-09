package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class SearchGenerator extends AbstractMetasearchGenerator<Result> {

    private static final long DEFAULT_TIMEOUT = 60000;

    private static final Result EMPTY_RESULT = Result.builder().id("").query(new SimpleQuery("")).build();

    private String timeout;

    public SearchGenerator(final MetaSearchService metaSearchService, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchService, saxStrategy);
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
    @Override
    @Deprecated
    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
    }

    @Override
    protected Result doSearch(final String query) {
        return searchWithEngines(query, null);
    }

    @Override
    protected Result getEmptyResult() {
        return EMPTY_RESULT;
    }

    protected Result searchWithEngines(final String query, final Collection<String> engines) {
        long wait = DEFAULT_TIMEOUT;
        if (null != this.timeout) {
            try {
                wait = Long.parseLong(this.timeout);
            } catch (NumberFormatException nfe) {
                wait = DEFAULT_TIMEOUT;
            }
        }
        return search(query, engines, wait);
    }
}
