package edu.stanford.irt.laneweb.search;

import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator implements CacheableProcessingComponent {

    public Serializable getKey() {
        return "describe";
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    @Override
    protected Result doSearch() {
        return this.metaSearchManager.describe(new SimpleQuery(""), null);
    }
}
