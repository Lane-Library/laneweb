package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;
import org.xml.sax.SAXException;

public abstract class CacheableEresourcesGenerator extends AbstractEresourcesGenerator implements CacheableProcessingComponent {

    private String key;

    private long configuredExpires = 1000 * 60 * 5;

    private long expires;

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        this.expires = par.getParameterAsLong("expires", this.configuredExpires);
    }

    public void setExpires(final long expires) {
        this.configuredExpires = expires;
    }

    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey();
        }
        return this.key;
    }

    public SourceValidity getValidity() {
        return new ExpiresValidity(this.expires);
    }

    private String createKey() {
        return new StringBuffer("t=").append(null == super.type ? "" : super.type).append(";s=").append(
                null == super.subset ? "" : super.subset).append(";a=").append(null == super.alpha ? "" : super.alpha).append(";m=")
                .append(null == super.mesh ? "" : super.mesh).toString();
    }

}
