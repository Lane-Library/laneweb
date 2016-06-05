package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class SearchGenerator extends AbstractMetasearchGenerator<Result> implements ParametersAware {

    private static final long DEFAULT_TIMEOUT = 60000;

    private static final Result EMPTY_RESULT = Result.newResultBuilder().id("").query(new SimpleQuery("")).build();

    private String timeout;

    public SearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchManager, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
    }

    @Override
    protected Result doSearch(final String query) {
        return searchWithEngines(query, null);
    }

    protected Result searchWithEngines(final String query, final Collection<String> engines) {
        Result result = null;
        if (query == null || query.isEmpty()) {
            result = EMPTY_RESULT;
        } else {
            long wait = DEFAULT_TIMEOUT;
            if (null != this.timeout) {
                try {
                    wait = Long.parseLong(this.timeout);
                } catch (NumberFormatException nfe) {
                    wait = DEFAULT_TIMEOUT;
                }
            }
            final SimpleQuery q = new SimpleQuery(query);
            result = search(q, engines, wait);
        }
        return result;
    }
}
