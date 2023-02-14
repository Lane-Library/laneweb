package edu.stanford.irt.laneweb.search;

import org.springframework.data.domain.Page;

public abstract class AbstractSolrSearchResult<T> {

    private Page<T> page;

    private String query;

    protected AbstractSolrSearchResult(final String query, final Page<T> page) {
        this.query = query;
        this.page = page;
    }

    public Page<T> getPage() {
        return this.page;
    }

    public String getQuery() {
        return this.query;
    }
}
