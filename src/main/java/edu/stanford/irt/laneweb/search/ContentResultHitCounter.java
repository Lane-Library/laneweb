package edu.stanford.irt.laneweb.search;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.search.impl.DefaultResult;

public class ContentResultHitCounter {

    private Map<String, String[]> resourceCounts;

    public ContentResultHitCounter(final DefaultResult result) {
        this.resourceCounts = new HashMap<String, String[]>();
        for (DefaultResult engine : result.getChildren()) {
            DefaultResult child = engine.getChildren().iterator().next();
            this.resourceCounts.put(child.getId(), new String[] { child.getURL(), child.getHits() });
        }
    }

    public Map<String, String[]> getResourceCounts() {
        return this.resourceCounts;
    }
}
