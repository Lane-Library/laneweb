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

    public ClearCacheAction(final Cache cache) {
        this.cache = cache;
    }

    @SuppressWarnings("rawtypes")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters parameters) {
        this.cache.clear();
        return Collections.emptyMap();
    }
}
