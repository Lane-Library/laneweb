package edu.stanford.irt.laneweb.cocoon.pipeline;

import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.components.pipeline.impl.ExpiresCachingProcessingPipeline;


public class ExpiresCachingPipeline extends ExpiresCachingProcessingPipeline {
    
    public void setCacheExpires(long cacheExpires) {
        this.cacheExpires = cacheExpires;
    }
    
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Laneweb pipelines have prototype scope, so won't be recycled.
     * 
     * @throws UnsupportedOperationException always
     */
    @Override
    public void recycle() {
        throw new UnsupportedOperationException();
    }
}
