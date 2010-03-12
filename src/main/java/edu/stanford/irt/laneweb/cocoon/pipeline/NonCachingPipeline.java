package edu.stanford.irt.laneweb.cocoon.pipeline;

import org.apache.cocoon.components.pipeline.impl.NonCachingProcessingPipeline;


public class NonCachingPipeline extends NonCachingProcessingPipeline {

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
