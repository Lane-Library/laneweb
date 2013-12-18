package edu.stanford.irt.laneweb.search;

import java.io.Serializable;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.legacy.LegacyMetaSearch;
import edu.stanford.irt.search.legacy.Result;

public class DescribeGenerator extends AbstractMetasearchGenerator implements CacheablePipelineComponent {

    private static final String TYPE = "describe";

    public DescribeGenerator(final LegacyMetaSearch metaSearchManager, final SAXStrategy<Result> saxStrategy) {
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
    protected Result doSearch(final String query) {
        return describe(new SimpleQuery(""));
    }
}
