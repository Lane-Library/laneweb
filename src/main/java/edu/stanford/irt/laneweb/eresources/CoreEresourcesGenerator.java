package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class CoreEresourcesGenerator extends CacheableEresourcesGenerator {

    public CoreEresourcesGenerator(final String componentType, CollectionManager collectionManager, SAXStrategy<PagingEresourceList> saxStrategy) {
        super(componentType, collectionManager, saxStrategy);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (this.type == null) {
            return Collections.emptySet();
        }
        return this.collectionManager.getCore(this.type);
    }
}
