package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public abstract class AbstractMetasearchGenerator<T> extends AbstractSearchGenerator<T> {

    private MetaSearchManager metaSearchManager;

    public AbstractMetasearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<T> saxStrategy) {
        super(saxStrategy);
        this.metaSearchManager = metaSearchManager;
    }

    public Result describe(final Query query, final Collection<String> engines) {
        return this.metaSearchManager.describe(query, engines);
    }

    public Result search(final Query query, final long arg1, final Collection<String> arg2, final boolean arg3) {
        return this.metaSearchManager.search(query, arg1, arg2, arg3);
    }
}
