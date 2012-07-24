package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class BrowseEresourcesGenerator extends AbstractEresourcesGenerator {

    public BrowseEresourcesGenerator(final String type, final CollectionManager collectionManager,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, collectionManager, saxStrategy);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        return getTypeOrSubset();
    }
}
