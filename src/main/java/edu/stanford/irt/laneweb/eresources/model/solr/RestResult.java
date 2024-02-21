package edu.stanford.irt.laneweb.eresources.model.solr;

import java.util.Collections;

import org.springframework.data.domain.Page;

import edu.stanford.irt.laneweb.eresources.model.Eresource;

public  class RestResult<T> {

    private Page<T> page;

    private String query;

    public static final RestResult<Eresource> EMPTY_RESULT = new RestResult<>(null, new RestPage<>(Collections.emptyList()));

    public RestResult(final String query, final Page<T> page) {
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
