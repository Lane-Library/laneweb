package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private Collection<String> engines;

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.engines = this.model.getObject(Model.ENGINES, Collection.class);
        if (this.engines == null) {
            String engineList = this.parameterMap.get("engine-list");
            if (engineList == null) {
                throw new IllegalArgumentException("null engine-list");
            }
            this.engines = Arrays.asList(engineList.split(","));
        }
    }
}
