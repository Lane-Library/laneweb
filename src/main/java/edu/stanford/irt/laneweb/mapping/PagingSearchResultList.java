package edu.stanford.irt.laneweb.mapping;

import java.util.Collection;

import edu.stanford.irt.laneweb.metasearch.SearchResult;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;

public class PagingSearchResultList extends PagingList<SearchResult> {

    private static final long serialVersionUID = 1L;

    private String query;

    public PagingSearchResultList(final Collection<SearchResult> collection, final PagingData pagingData,
            final String query) {
        super(collection, pagingData);
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
