package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;

public class CoreEresourcesGenerator extends CacheableEresourcesGenerator {

    public CoreEresourcesGenerator(final String componentType, CollectionManager collectionManager, SAXStrategy<PagingEresourceList> saxStrategy) {
        super(componentType, collectionManager, saxStrategy);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null == this.type) {
            //TODO: return empty collection
            throw new LanewebException("null type");
        }
        return this.collectionManager.getCore(this.type);
    }
}
