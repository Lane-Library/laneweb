package edu.stanford.irt.laneweb.search;

import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.spring.SearchCacheManager;

public class SearchCacheManagerAction extends AbstractAction {

    private SearchCacheManager searchCache;

    public Map<String, String> doAct() {
        String query = this.model.getString(Model.QUERY);
        if (query != null) {
            this.searchCache.clearCache(query);
        } else {
            this.searchCache.clearAllCaches();
        }
        return null;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        if (null == msms) {
            throw new IllegalArgumentException("null metaSearchManagerSource");
        }
        this.searchCache = msms.getSearchCacheManager();
        if (null == this.searchCache) {
            throw new IllegalStateException("null searchCacheManager");
        }
    }
}
