package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private Collection<String> engines;

    public EngineSearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<Result> saxStrategy) {
        super(metaSearchManager, saxStrategy);
    }

    @Override
    protected Result doSearch(final String query) {
        return searchWithEngines(query, this.engines);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (this.engines == null) {
            String engineList = parameters.get(Model.ENGINES);
            if (engineList == null) {
                //TODO: maybe null engines is OK
                throw new LanewebException("null engines");
            }
            this.engines = Arrays.asList(engineList.split(","));
        }
    }
}
