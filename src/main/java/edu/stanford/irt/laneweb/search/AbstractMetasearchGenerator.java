package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.DefaultResult;

public abstract class AbstractMetasearchGenerator extends AbstractSearchGenerator<DefaultResult> {

    private MetaSearchManager<DefaultResult> metaSearchManager;

    public AbstractMetasearchGenerator(final MetaSearchManager<DefaultResult> metaSearchManager, final SAXStrategy<DefaultResult> saxStrategy) {
        super(saxStrategy);
        this.metaSearchManager = metaSearchManager;
    }

    public DefaultResult describe(final Query query, final Collection<String> engines) {
        return this.metaSearchManager.describe(query, engines);
    }

    public DefaultResult search(final Query query, final long arg1, final Collection<String> arg2, final boolean arg3) {
        return this.metaSearchManager.search(query, arg1, arg2, arg3);
    }
}
