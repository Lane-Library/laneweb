package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {
    
    private static final String[] EMPTY_ENGINES = new String[0];
    
    private Collection<String> engines = Collections.emptySet();

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        super.setup(resolver, objectModel, src, par);
        String[] engines = getObject("engines", String[].class, EMPTY_ENGINES);
        this.engines = new HashSet(engines.length);
        this.engines.addAll(Arrays.asList(engines));
    }
}
