package edu.stanford.irt.laneweb.search;

import java.io.Serializable;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator implements CacheableProcessingComponent {

    private static final String TYPE = "describe";

    public Serializable getKey() {
        return TYPE;
    }

    public String getType() {
        return TYPE;
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    //need to override this otherwise throw exception because null query
    @Override
    public void setModel(final Map<String, Object> model) {
    }

    @Override
    protected Result doSearch() {
        return this.metaSearchManager.describe(new SimpleQuery(""), null);
    }
}
