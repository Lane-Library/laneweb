package edu.stanford.irt.laneweb.cocoon.pipeline;

import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.components.pipeline.impl.CachingProcessingPipeline;


public class CachingPipeline extends CachingProcessingPipeline {
    
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
