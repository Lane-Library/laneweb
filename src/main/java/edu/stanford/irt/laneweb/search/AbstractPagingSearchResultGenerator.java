package edu.stanford.irt.laneweb.search;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;

public abstract class AbstractPagingSearchResultGenerator extends AbstractSearchGenerator<PagingSearchResultList> {

    private int page;
    private String urlEncodedQuery;

    public AbstractPagingSearchResultGenerator(final SAXStrategy<PagingSearchResultList> saxStrategy) {
        super(saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        String p = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = "all".equals(p) ? -1 : Integer.parseInt(p) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
        this.urlEncodedQuery = ModelUtil.getString(model, Model.URL_ENCODED_QUERY);
    }

    @Override
    protected PagingSearchResultList doSearch(final String query) {
        List<SearchResult> results = null;
        if (query != null && !query.isEmpty()) {
            results = getSearchResults(query);
            Collections.sort(results);
            // de-duplicate results (remove scopus articles when pubmed version present)
            // TODO: find a more robust method of doing this.
            SearchResult previous = null;
            for (Iterator<SearchResult> it = results.iterator(); it.hasNext();) {
                SearchResult next = it.next();
                if (previous != null && next.equals(previous)) {
                    it.remove();
                } else {
                    previous = next;
                }
            }
        } else {
            results = Collections.emptyList();
        }
        PagingData pagingData = new PagingData(results, this.page, "q=" + this.urlEncodedQuery);
        return new PagingSearchResultList(results, pagingData, query);
    }

    protected abstract List<SearchResult> getSearchResults(String query);
}
