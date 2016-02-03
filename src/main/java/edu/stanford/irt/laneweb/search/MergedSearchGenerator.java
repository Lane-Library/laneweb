package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.solr.SolrService;
import edu.stanford.irt.search.impl.MetaSearchManager;

/**
 * @author ryanmax
 */
public class MergedSearchGenerator extends ContentSearchGenerator {

    private SolrService solrService;

    public MergedSearchGenerator(final MetaSearchManager metaSearchManager, final SolrService solrService,
            final SAXStrategy<PagingSearchResultList> saxStrategy,
            final ContentResultConversionStrategy scoreStrategy) {
        super(metaSearchManager, saxStrategy, scoreStrategy);
        this.solrService = solrService;
    }

    @Override
    protected List<SearchResult> getSearchResults(final String query) {
        List<SearchResult> searchResults = null;
        if (query == null || query.isEmpty()) {
            searchResults = Collections.emptyList();
        } else {
            searchResults = super.getSearchResults(query);
            List<SearchResult> eresourceResults = new ArrayList<>();
            for (Eresource eresource : this.solrService.search(query)) {
                eresourceResults.add(new EresourceSearchResult(eresource));
            }
            searchResults.addAll(eresourceResults);
        }
        return searchResults;
    }
}
