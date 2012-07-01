package edu.stanford.irt.laneweb.search;

import java.util.TreeSet;

public class PagingSearchResultSet extends TreeSet<SearchResult> {

    private static final long serialVersionUID = 1L;

    private int page;

    private String query;

    public PagingSearchResultSet(final String query, final int page) {
        this.query = query;
        this.page = page;
    }

    public int getPage() {
        return this.page;
    }

    public String getQuery() {
        return this.query;
    }
}
