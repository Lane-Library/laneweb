package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.search.AbstractSearchGenerator;
import edu.stanford.irt.search.impl.Result;

public abstract class AbstractMetasearchGenerator<T> extends AbstractSearchGenerator<T> {

    private MetaSearchService metaSearchService;

    protected AbstractMetasearchGenerator(final MetaSearchService metaSearchService, final SAXStrategy<T> saxStrategy) {
        super(saxStrategy);
        this.metaSearchService = metaSearchService;
    }

    public Result describe(final String query, final Collection<String> engines) {
        return this.metaSearchService.describe(query, engines);
    }

    public Result search(final String query, final Collection<String> engines, final long wait) {
        return this.metaSearchService.search(query, engines, wait);
    }
}
