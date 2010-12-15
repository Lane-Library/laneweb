package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.eresources.Eresource;

public class MeSHEresourcesGenerator extends CacheableEresourcesGenerator {

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null == this.mesh) {
            throw new IllegalStateException("null mesh");
        }
        if (null == this.type) {
            throw new IllegalStateException("null type");
        }
        return (Collection<Eresource>) this.collectionManager.getMesh(this.type, this.mesh);
    }
}
