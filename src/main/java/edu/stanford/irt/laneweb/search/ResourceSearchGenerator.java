package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResourceSearchGenerator extends SearchGenerator {
    
    private static final String[] EMPTY_RESOURCES = new String[0];

    private Collection<String> resources = Collections.emptySet();

    @Override
    public Result doSearch() {
        Collection<String> engineToRun = new LinkedList<String>();
        Query query = new SimpleQuery(super.query);
        Result describeResult = this.metaSearchManager.describe(query, null);

        for (String resource : this.resources) {
            for (Result result : describeResult.getChildren()) {
                if (result.getChild(resource) != null) {
                    engineToRun.add(result.getId());
                    break;
                }
            }
        }
        return super.doSearch(engineToRun);
    }

    @Override
    public void initialize() {
        super.initialize();
        String[] resources = this.model.getObject("resources", String[].class, EMPTY_RESOURCES);
        this.resources = new HashSet(resources.length);
        this.resources.addAll(Arrays.asList(resources));
    }
}
