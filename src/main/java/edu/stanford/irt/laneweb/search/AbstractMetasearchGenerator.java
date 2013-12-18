package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.legacy.LegacyMetaSearch;
import edu.stanford.irt.search.legacy.Result;

public abstract class AbstractMetasearchGenerator extends AbstractSearchGenerator<Result> {

    private LegacyMetaSearch LegacyMetaSearch;

    public AbstractMetasearchGenerator(final LegacyMetaSearch metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(saxStrategy);
        this.LegacyMetaSearch = metaSearchManager;
    }

    public Result describe(final Query query) {
        return this.LegacyMetaSearch.describe(query);
    }

    public Result search(final Query query, final long arg1, final boolean arg3) {
        return this.LegacyMetaSearch.search(query, arg1, arg3);
    }
}
