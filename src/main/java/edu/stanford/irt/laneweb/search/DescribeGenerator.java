package edu.stanford.irt.laneweb.search;

import java.io.Serializable;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator implements CacheablePipelineComponent {

    private static final String TYPE = "describe";

    public DescribeGenerator(final MetaSearchManager<DefaultResult> metaSearchManager, final SAXStrategy<DefaultResult> saxStrategy) {
        super(metaSearchManager, saxStrategy);
    }

    public Serializable getKey() {
        return TYPE;
    }

    public String getType() {
        return TYPE;
    }

    public Validity getValidity() {
        return AlwaysValid.SHARED_INSTANCE;
    }

    @Override
    protected DefaultResult doSearch(final String query) {
        return describe(new SimpleQuery(""), null);
    }
}
