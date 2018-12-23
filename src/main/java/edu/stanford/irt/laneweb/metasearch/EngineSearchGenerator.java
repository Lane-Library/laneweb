package edu.stanford.irt.laneweb.metasearch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private Collection<String> engines;

    public EngineSearchGenerator(final MetaSearchService metaSearchService, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchService, saxStrategy);
    }

    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class);
    }

    @Override
    @Deprecated
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (this.engines == null) {
            String engineList = Objects.requireNonNull(parameters.get(Model.ENGINES), "null engines");
            this.engines = Arrays.asList(engineList.split(","));
        }
    }

    @Override
    protected Result doSearch(final String query) {
        return searchWithEngines(query, this.engines);
    }
}
