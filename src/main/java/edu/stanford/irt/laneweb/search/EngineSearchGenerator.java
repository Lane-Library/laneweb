package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {
    
    private Collection<String> engines;

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @Override
    public void initialize() {
        super.initialize();
        this.engines = this.model.getObject(LanewebObjectModel.ENGINES, Collection.class, Collections.<String>emptyList());
    }
}
