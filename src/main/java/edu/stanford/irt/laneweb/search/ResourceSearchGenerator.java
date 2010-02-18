package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResourceSearchGenerator extends SearchGenerator {

    private Collection<String> resources;

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
        this.resources = this.model.getObject(LanewebObjectModel.RESOURCES, Collection.class, Collections.<String>emptyList());

    }
}
