package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;

public class BrowseEresourcesGenerator extends CacheableEresourcesGenerator {

    public BrowseEresourcesGenerator(final String type, CollectionManager collectionManager, SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, collectionManager, saxStrategy);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        Collection<Eresource> list = null;
        if (this.subset == null && this.type == null) {
            //TODO: maybe return an empty collection
            throw new LanewebException("null type and subset");
        } else if (this.subset == null && this.alpha == null) {
            list = this.collectionManager.getType(this.type);
        } else if (this.subset == null) {
            list = this.collectionManager.getType(this.type, this.alpha.charAt(0));
        } else {
            list = this.collectionManager.getSubset(this.subset);
        }
        return list;
    }
}
