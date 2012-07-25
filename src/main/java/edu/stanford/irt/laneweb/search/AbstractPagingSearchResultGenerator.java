package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractPagingSearchResultGenerator extends AbstractSearchGenerator<PagingSearchResultSet> implements
        ModelAware {

    private int page;

    public AbstractPagingSearchResultGenerator(final SAXStrategy<PagingSearchResultSet> saxStrategy) {
        super(saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String page = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
    }

    @Override
    protected PagingSearchResultSet doSearch(final String query) {
        PagingSearchResultSet result = new PagingSearchResultSet(query, this.page);
        if (query != null) {
            result.addAll(getSearchResults(query));
        }
        return result;
    }

    protected abstract Collection<SearchResult> getSearchResults(String query);
}
