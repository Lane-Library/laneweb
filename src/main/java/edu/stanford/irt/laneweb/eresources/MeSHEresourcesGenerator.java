package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;

import edu.stanford.irt.eresources.Eresource;

public class MeSHEresourcesGenerator extends CacheableEresourcesGenerator {

    public MeSHEresourcesGenerator(final String componentType) {
        super(componentType);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (this.mesh == null || this.type == null) {
            return Collections.emptySet();
        }
        return this.collectionManager.getMesh(this.type, this.mesh);
    }
}
