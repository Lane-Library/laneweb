package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;

import edu.stanford.irt.search.impl.Result;

public interface MetaSearchService {

    void clearAllCaches();

    void clearCache(String q);

    Result describe(String query, Collection<String> engines);

    Result search(String query, Collection<String> engines, long wait);

    byte[] testURL(String url);
}
