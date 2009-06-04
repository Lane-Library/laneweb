package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;

public abstract class CacheableEresourcesGenerator extends AbstractEresourcesGenerator implements CacheableProcessingComponent {

    private long configuredExpires = 1000 * 60 * 5;

    private long expires;

    private String key;

    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey();
        }
        return this.key;
    }

    public SourceValidity getValidity() {
        return new ExpiresValidity(this.expires);
    }

    public void setExpires(final long expires) {
        this.configuredExpires = expires;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        super.setup(resolver, objectModel, src, par);
        this.expires = par.getParameterAsLong("expires", this.configuredExpires);
    }

    private String createKey() {
        return new StringBuffer("t=").append(null == super.type ? "" : super.type).append(";s=").append(null == super.subset ? "" : super.subset).append(";a=")
                .append(null == super.alpha ? "" : super.alpha).append(";m=").append(null == super.mesh ? "" : super.mesh).toString();
    }
}
