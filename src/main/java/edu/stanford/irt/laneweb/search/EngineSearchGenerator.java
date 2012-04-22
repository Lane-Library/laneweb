package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private Collection<String> engines;
    
    private Map<String, String> parameters;

    private Map<String, Object> model;
    
    public void setParameters(Map<String, String> parameters) {
        super.setParameters(parameters);
        this.parameters = parameters;
    }
    
    public void setModel(Map<String, Object> model) {
        super.setModel(model);
        this.model = model;
    }

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
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
}
