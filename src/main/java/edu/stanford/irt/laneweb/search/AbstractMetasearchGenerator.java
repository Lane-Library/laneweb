package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.Result;

public abstract class AbstractMetasearchGenerator<T> extends AbstractSearchGenerator<T> {

    private MetaSearchService metaSearchService;

    public AbstractMetasearchGenerator(final MetaSearchService metaSearchService, final SAXStrategy<T> saxStrategy) {
        super(saxStrategy);
        this.metaSearchService = metaSearchService;
    }

    public Result describe(final Query query, final Collection<String> engines) {
        return this.metaSearchService.describe(query, engines);
    }

    public Result search(final Query query, final Collection<String> engines, final long wait) {
        return this.metaSearchService.search(query, engines, wait);
    }
}
