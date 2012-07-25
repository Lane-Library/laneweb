package edu.stanford.irt.laneweb.search;

import java.io.Serializable;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator implements CacheableProcessingComponent {

    private static final String TYPE = "describe";

    public DescribeGenerator(final MetaSearchManagerSource msms, final SAXStrategy<Result> saxStrategy) {
        super(msms, saxStrategy);
    }

    public DescribeGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchManager, saxStrategy);
    }

    public Serializable getKey() {
        return TYPE;
    }

    public String getType() {
        return TYPE;
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    @Override
    protected Result doSearch(final String query) {
        return describe(new SimpleQuery(""), null);
    }
}
