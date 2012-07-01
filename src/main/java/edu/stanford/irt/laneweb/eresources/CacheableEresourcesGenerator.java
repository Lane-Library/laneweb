package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.model.Model;

public abstract class CacheableEresourcesGenerator extends AbstractEresourcesGenerator implements CacheableProcessingComponent {
    
    private static final long DEFAULT_EXPIRES = 1000 * 60 * 5;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private String key;
    
    private SourceValidity validity;

    public CacheableEresourcesGenerator(final String componentType, CollectionManager collectionManager, SAXStrategy<PagingEresourceList> saxStrategy) {
        super(collectionManager, saxStrategy);
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
        if (this.validity == null) {
            this.validity = new ExpiresValidity(this.expires);
        }
        return this.validity;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.EXPIRES)) {
            this.expires = Long.parseLong(parameters.get(Model.EXPIRES));
        }
    }

    private String createKey() {
        return new StringBuilder("t=").append(null == super.type ? "" : super.type).append(";s=")
                .append(null == super.subset ? "" : super.subset).append(";a=").append(null == super.alpha ? "" : super.alpha)
                .append(";m=").append(null == super.mesh ? "" : super.mesh).append(";page=").append(this.page).toString();
    }
}
