package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.eresources.Eresource;

public class BrowseEresourcesGenerator extends CacheableEresourcesGenerator {

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null != this.type) {
            if (null != this.alpha) {
                return this.collectionManager.getType(this.type, this.alpha.charAt(0));
            }
            return this.collectionManager.getType(this.type);
        } else if (null != this.subset) {
            return this.collectionManager.getSubset(this.subset);
        } else {
            throw new IllegalStateException("null type or subset");
        }
    }

}
