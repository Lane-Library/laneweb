package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchable;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.Result;

public abstract class AbstractMetasearchGenerator extends AbstractSearchGenerator<Result> {

    private MetaSearchable<Result> metaSearchable;

    public AbstractMetasearchGenerator(final MetaSearchable<Result> metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(saxStrategy);
        this.metaSearchable = metaSearchManager;
    }

    public Result describe(final Query query, final Collection<String> engines) {
        return this.metaSearchable.describe(query, engines);
    }

    public Result search(final Query query, final long arg1, final Collection<String> arg2, final boolean arg3) {
        return this.metaSearchable.search(query, arg1, arg2, arg3);
    }
}
