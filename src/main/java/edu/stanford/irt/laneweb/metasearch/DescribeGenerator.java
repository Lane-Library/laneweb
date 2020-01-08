package edu.stanford.irt.laneweb.metasearch;

import java.io.Serializable;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.search.impl.Result;

public class DescribeGenerator extends AbstractGenerator {

    private static final String EMPTY_QUERY = "";

    private static final String TYPE = "describe";

    private MetaSearchService metaSearchService;

    private SAXStrategy<Result> saxStrategy;

    public DescribeGenerator(final MetaSearchService metaSearchService, final SAXStrategy<Result> saxStrategy) {
        this.metaSearchService = metaSearchService;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public Serializable getKey() {
        return getType();
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
        this.saxStrategy.toSAX(this.metaSearchService.describe(EMPTY_QUERY, null), xmlConsumer);
    }
}
