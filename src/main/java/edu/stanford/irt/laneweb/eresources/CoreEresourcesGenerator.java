package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.eresources.Eresource;

public class CoreEresourcesGenerator extends CacheableEresourcesGenerator {

    public CoreEresourcesGenerator(final String componentType) {
        super(componentType);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null == this.type) {
            throw new IllegalStateException("null type");
        }
        return this.collectionManager.getCore(this.type);
    }
}
