package edu.stanford.irt.laneweb.search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.search.MetaSearchable;
import edu.stanford.irt.search.impl.Result;

/**
 * @author ryanmax
 */
public class MergedSearchGenerator extends ContentSearchGenerator {

    private CollectionManager collectionManager;

    public MergedSearchGenerator(final MetaSearchable<Result> metaSearchManager, final CollectionManager collectionManager,
            final SAXStrategy<PagingSearchResultList> saxStrategy, final ContentResultConversionStrategy scoreStrategy) {
        super(metaSearchManager, saxStrategy, scoreStrategy);
        this.collectionManager = collectionManager;
    }

    @Override
    protected List<SearchResult> getSearchResults(final String query) {
        List<SearchResult> searchResults = null;
        if (query == null || query.isEmpty()) {
            searchResults = Collections.emptyList();
        } else {
            searchResults = super.getSearchResults(query);
            List<SearchResult> eresourceResults = new LinkedList<SearchResult>();
            for (Eresource eresource : this.collectionManager.search(query)) {
                eresourceResults.add(new EresourceSearchResult(eresource));
            }
            searchResults.addAll(eresourceResults);
        }
        return searchResults;
    }
}
