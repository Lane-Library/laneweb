package edu.stanford.irt.laneweb.metasearch;

import java.io.Serializable;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractGenerator implements CacheablePipelineComponent {

    private static final Query EMPTY_QUERY = new SimpleQuery("");

    private static final String TYPE = "describe";

    private MetaSearchManager metaSearchManager;

    private SAXStrategy<Result> saxStrategy;

    public DescribeGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        this.metaSearchManager = metaSearchManager;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public Serializable getKey() {
        return TYPE;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Validity getValidity() {
        return AlwaysValid.SHARED_INSTANCE;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(this.metaSearchManager.describe(EMPTY_QUERY, null), xmlConsumer);
    }
}
