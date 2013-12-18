package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.legacy.LegacyMetaSearch;
import edu.stanford.irt.search.legacy.Result;

public class SearchGenerator extends AbstractMetasearchGenerator implements ParametersAware {

    private static final long DEFAULT_TIMEOUT = 60000;

    private String synchronous;

    private String timeout;

    private String wait;

    public SearchGenerator(final LegacyMetaSearch metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchManager, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
        this.wait = ModelUtil.getString(model, "wait");
        this.synchronous = ModelUtil.getString(model, Model.SYNCHRONOUS);
    }

    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
        if (this.synchronous == null) {
            this.synchronous = parameters.get(Model.SYNCHRONOUS);
        }
    }

    @Override
    protected Result doSearch(final String query) {
        return searchWithEngines(query, null);
    }

    protected Result searchWithEngines(final String query, final Collection<String> engines) {
        Result result = null;
        if (query == null || query.isEmpty()) {
            result = new Result("null");
            result.setStatus(SearchStatus.FAILED);
        } else {
            long searchTimeout = DEFAULT_TIMEOUT;
            if (null != this.timeout) {
                try {
                    searchTimeout = Long.parseLong(this.timeout);
                } catch (NumberFormatException nfe) {
                    searchTimeout = DEFAULT_TIMEOUT;
                }
            }
            boolean sync = Boolean.parseBoolean(this.synchronous);
            final SimpleQuery q = new SimpleQuery(query, engines);
            result = search(q, searchTimeout, sync);
            if (null != this.wait) {
                long wt = 0;
                try {
                    wt = Long.parseLong(this.wait);
                } catch (NumberFormatException nfe) {
                    wt = 0;
                }
                long start = System.currentTimeMillis();
                try {
                    synchronized (result) {
                        while ((wt > 0) && SearchStatus.RUNNING.equals(result.getStatus())) {
                            result.wait(wt);
                            if (wt != 0) {
                                long now = System.currentTimeMillis();
                                wt = wt - (now - start);
                                start = now;
                            }
                        }
                    }
                } catch (InterruptedException ie) {
                    throw new IllegalStateException(ie);
                }
            }
        }
        return result;
    }
}
