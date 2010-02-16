package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.eresources.Eresource;

public class SearchEresourcesGenerator extends AbstractEresourcesGenerator {

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null == this.query) {
            throw new IllegalStateException("null query");
        }
        if (null != this.type) {
            return this.collectionManager.searchType(this.type, this.query);
        } else if (null != this.subset) {
            return this.collectionManager.searchSubset(this.subset, this.query);
        }
        return this.collectionManager.search(this.query);
    }
}
