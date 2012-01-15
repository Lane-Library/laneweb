package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private Collection<String> engines;

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        super.initialize();
        this.engines = ModelUtil.getObject(getModel(), Model.ENGINES, Collection.class);
        if (this.engines == null) {
            String engineList = getParameterMap().get(Model.ENGINES);
            if (engineList == null) {
                throw new IllegalArgumentException("null engines");
            }
            this.engines = Arrays.asList(engineList.split(","));
        }
    }
}
