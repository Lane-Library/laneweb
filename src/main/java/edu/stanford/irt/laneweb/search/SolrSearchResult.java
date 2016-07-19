package edu.stanford.irt.laneweb.search;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.solr.Eresource;

public class SolrSearchResult extends AbstractSolrSearchResult<Eresource> {

    public static final SolrSearchResult EMPTY_RESULT = new SolrSearchResult(null,
            new SolrResultPage<Eresource>(Collections.emptyList()));;

    public SolrSearchResult(final String query, final Page<Eresource> page) {
        super(query, page);
    }
}
