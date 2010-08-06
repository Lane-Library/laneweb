package edu.stanford.irt.laneweb.cocoon;

import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;


public class ClearCacheAction implements Action {
    
    private Cache cache;

    public ClearCacheAction(Cache cache) {
        this.cache = cache;
    }

    @SuppressWarnings("rawtypes")
    public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) {
        this.cache.clear();
        return Collections.emptyMap();
    }
}
