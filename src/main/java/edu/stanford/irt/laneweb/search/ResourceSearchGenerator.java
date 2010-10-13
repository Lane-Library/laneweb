package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResourceSearchGenerator extends SearchGenerator {

    private Collection<String> resources;

    @Override
    public Result doSearch() {
        Collection<String> enginesToRun = new HashSet<String>();
        Result describeResult = this.metaSearchManager.describe(new SimpleQuery(this.query), null);
        Map<String, String> enginesMap = new HashMap<String, String>();
        for (Result engine : describeResult.getChildren()) {
            for (Result resource : engine.getChildren()) {
                enginesMap.put(resource.getId(), engine.getId());
            }
        }
        for (String resource : this.resources) {
            if (enginesMap.containsKey(resource)) {
                enginesToRun.add(enginesMap.get(resource));
            }
        }
        return super.doSearch(enginesToRun);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initialize() {
        super.initialize();
        this.resources = getObject(this.model, Model.RESOURCES, Collection.class);
        if (this.resources == null) {
            String resourceList = this.parameterMap.get("resource-list");
            if (resourceList == null) {
                throw new IllegalArgumentException("null resource-list");
            }
            this.resources = Arrays.asList(resourceList.split(","));
        }
    }
}
