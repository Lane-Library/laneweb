package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private Collection<String> engines;

    private Map<String, Object> model;

    private Map<String, String> parameters;

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize() {
        super.initialize();
        this.engines = ModelUtil.getObject(this.model, Model.ENGINES, Collection.class);
        if (this.engines == null) {
            String engineList = this.parameters.get(Model.ENGINES);
            if (engineList == null) {
                throw new IllegalArgumentException("null engines");
            }
            this.engines = Arrays.asList(engineList.split(","));
        }
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.model = model;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        this.parameters = parameters;
    }
}
