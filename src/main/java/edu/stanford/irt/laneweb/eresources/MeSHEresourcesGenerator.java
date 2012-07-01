package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class MeSHEresourcesGenerator extends CacheableEresourcesGenerator {

    public MeSHEresourcesGenerator(final String componentType, CollectionManager collectionManager, SAXStrategy<PagingEresourceList> saxStrategy) {
        super(componentType, collectionManager, saxStrategy);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (this.mesh == null || this.type == null) {
            return Collections.emptySet();
        }
        return this.collectionManager.getMesh(this.type, this.mesh);
    }
}
