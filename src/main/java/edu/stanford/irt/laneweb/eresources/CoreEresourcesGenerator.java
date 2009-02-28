package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.eresources.Eresource;

public class CoreEresourcesGenerator extends CacheableEresourcesGenerator {

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null == this.type) {
            throw new IllegalStateException("null type");
        } else if (null != this.mesh) {
            return this.collectionManager.getMeshCore(this.type, this.mesh);
        }
        return this.collectionManager.getCore(this.type);
    }
}
