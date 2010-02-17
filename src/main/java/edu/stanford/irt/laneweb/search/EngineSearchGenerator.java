package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {
    
    private static final String[] EMPTY_ENGINES = new String[0];
    
    private Collection<String> engines = Collections.emptySet();

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @Override
    public void initialize() {
        super.initialize();
        String[] engines = this.model.getObject("engines", String[].class, EMPTY_ENGINES);
        this.engines = new HashSet(engines.length);
        this.engines.addAll(Arrays.asList(engines));
    }
}
