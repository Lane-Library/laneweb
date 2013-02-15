package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;


public class PagingSearchResultList extends PagingList<SearchResult> {

    private String query;

    public PagingSearchResultList(Collection<SearchResult> collection, PagingData pagingData, String query) {
        super(collection, pagingData);
        this.query = query;
    }
    
    public String getQuery() {
        return this.query;
    }

    private static final long serialVersionUID = 1L;
}
