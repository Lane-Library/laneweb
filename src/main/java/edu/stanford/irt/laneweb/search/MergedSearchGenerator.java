package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.search.MetaSearchManager;

/**
 * @author ryanmax
 */
public class MergedSearchGenerator extends ContentSearchGenerator {

    private CollectionManager collectionManager;

    public MergedSearchGenerator(final MetaSearchManager metaSearchManager, final CollectionManager collectionManager,
            final SAXStrategy<PagingSearchResultSet> saxStrategy, final ContentResultConversionStrategy scoreStrategy) {
        super(metaSearchManager, saxStrategy, scoreStrategy);
        this.collectionManager = collectionManager;
    }

    @Override
    protected Collection<SearchResult> getSearchResults(final String query) {
        Collection<SearchResult> searchResults = null;
        if (query == null || query.isEmpty()) {
            searchResults = Collections.emptySet();
        } else {
            searchResults = super.getSearchResults(query);
            Collection<SearchResult> eresourceResults = new LinkedList<SearchResult>();
            for (Eresource eresource : this.collectionManager.search(query)) {
                eresourceResults.add(new EresourceSearchResult(eresource));
            }
            searchResults.addAll(eresourceResults);
        }
        return searchResults;
    }
}
