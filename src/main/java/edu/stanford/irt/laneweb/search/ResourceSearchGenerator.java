package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResourceSearchGenerator extends SearchGenerator {

    private Collection<String> resources;

    @Override
    public Result doSearch() {
        Collection<String> engineToRun = new HashSet<String>();
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

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        super.initialize();
        this.resources = this.model.getObject(Model.RESOURCES, Collection.class);
        if (this.resources == null) {
            String resourceList = this.parameterMap.get("resource-list");
            if (resourceList == null) {
                throw new IllegalArgumentException("null resource-list");
            }
            this.resources = Arrays.asList(resourceList.split(","));
        }
    }
}
