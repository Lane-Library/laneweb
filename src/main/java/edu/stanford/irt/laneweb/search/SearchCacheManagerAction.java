package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.spring.SearchCacheManager;

public class SearchCacheManagerAction implements Action {

    private SearchCacheManager searchCache;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel,
            final String string, final Parameters param) {
        String query = param.getParameter("query", null);
        if (query != null && !"".equals(query.trim())) {
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
