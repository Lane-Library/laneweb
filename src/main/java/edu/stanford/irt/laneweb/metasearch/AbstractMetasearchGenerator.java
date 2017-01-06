package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.search.AbstractSearchGenerator;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public abstract class AbstractMetasearchGenerator<T> extends AbstractSearchGenerator<T> {

    private MetaSearchManager metaSearchManager;

    public AbstractMetasearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<T> saxStrategy) {
        super(saxStrategy);
        this.metaSearchManager = metaSearchManager;
    }

    public Result describe(final Query query, final Collection<String> engines) {
        return this.metaSearchManager.describe(query, engines);
    }

    public Result search(final Query query, final Collection<String> engines, final long wait) {
        return this.metaSearchManager.search(query, engines, wait);
    }
}
