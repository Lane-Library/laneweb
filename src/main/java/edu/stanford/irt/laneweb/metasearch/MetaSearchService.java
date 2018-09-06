package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;

import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.status.StatusService;

public interface MetaSearchService extends StatusService {

    void clearAllCaches();

    void clearCache(String q);

    Result describe(String query, Collection<String> engines);

    Result search(String query, Collection<String> engines, long wait);

    byte[] testURL(String url);
}
