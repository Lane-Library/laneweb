package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;

public abstract class CacheableEresourcesGenerator extends AbstractEresourcesGenerator implements CacheableProcessingComponent {

    private String componentType;

    private long configuredExpires = 1000 * 60 * 5;

    private long expires;

    private String key;

    public CacheableEresourcesGenerator(final String componentType) {
        this.componentType = componentType;
    }

    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey();
        }
        return this.key;
    }

    public String getType() {
        return this.componentType;
    }

    public SourceValidity getValidity() {
        return new ExpiresValidity(this.expires);
    }

    public void setExpires(final long expires) {
        this.configuredExpires = expires;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        this.expires = parameters.containsKey("expires") ? Long.parseLong(parameters.get("expires")) : this.configuredExpires;
    }

    private String createKey() {
        return new StringBuilder("t=").append(null == super.type ? "" : super.type).append(";s=")
                .append(null == super.subset ? "" : super.subset).append(";a=").append(null == super.alpha ? "" : super.alpha)
                .append(";m=").append(null == super.mesh ? "" : super.mesh).append(";page=").append(this.page).toString();
    }
}
