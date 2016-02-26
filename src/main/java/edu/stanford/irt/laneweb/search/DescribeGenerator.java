package edu.stanford.irt.laneweb.search;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator<Result> implements CacheablePipelineComponent {

    private static final String TYPE = "describe";

    public DescribeGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchManager, saxStrategy);
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
    protected Result doSearch(final String query) {
        return describe(new SimpleQuery(""), null);
    }
    
    @Override
    public void setModel(final Map<String, Object> model) {
        model.put(Model.QUERY, "");
        super.setModel(model);
    }
    
}
