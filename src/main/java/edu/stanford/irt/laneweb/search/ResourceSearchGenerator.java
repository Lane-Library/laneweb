package edu.stanford.irt.laneweb.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResourceSearchGenerator extends SearchGenerator {

    private Collection<String> resources = Collections.emptySet();

    public ResourceSearchGenerator(final MetaSearchManager<DefaultResult> metaSearchManager, final SAXStrategy<DefaultResult> saxStrategy) {
        super(metaSearchManager, saxStrategy);
    }

    @Override
    public DefaultResult doSearch(final String query) {
        String q = query == null ? "" : query;
        Collection<String> enginesToRun = new HashSet<String>();
        DefaultResult describeResult = describe(new SimpleQuery(q), null);
        Map<String, String> enginesMap = new HashMap<String, String>();
        for (DefaultResult engine : describeResult.getChildren()) {
            String engineId = engine.getId();
            for (DefaultResult resource : engine.getChildren()) {
                enginesMap.put(resource.getId(), engineId);
            }
        }
        for (String resource : this.resources) {
            if (enginesMap.containsKey(resource)) {
                enginesToRun.add(enginesMap.get(resource));
            }
        }
        return super.searchWithEngines(query, enginesToRun);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.resources = ModelUtil.getObject(model, Model.RESOURCES, Collection.class);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (this.resources == null) {
            String resourceList = parameters.get("resource-list");
            if (resourceList == null) {
                throw new LanewebException("null resource-list");
            }
            this.resources = Arrays.asList(resourceList.split(","));
        }
    }
}
