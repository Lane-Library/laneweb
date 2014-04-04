package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public abstract class AbstractMetasearchGenerator<T> extends AbstractSearchGenerator<T> {

    private MetaSearchManager metaSearchManager;

    public AbstractMetasearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<T> saxStrategy) {
        super(saxStrategy);
        this.metaSearchManager = metaSearchManager;
    }

    public Result describe(final Query query) {
        return this.metaSearchManager.describe(query);
    }

    public Result search(final Query query, final long arg1, final boolean arg3) {
        return this.metaSearchManager.search(query, arg1, arg3);
    }
}
