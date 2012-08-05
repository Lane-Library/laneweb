package edu.stanford.irt.cocoon.pipeline.transform;

import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

public abstract class AbstractCacheableTransformer extends AbstractTransformer implements CacheableProcessingComponent {

    private String type;

    public AbstractCacheableTransformer(final String type) {
        this.type = type;
    }

    public Serializable getKey() {
        return this.type;
    }

    public String getType() {
        return this.type;
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }
}
