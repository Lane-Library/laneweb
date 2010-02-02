package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResourceSearchGenerator extends SearchGenerator {

    private Collection<String> resources;

    @Override
    public Result doSearch() {
        Collection<String> engineToRun = new HashSet<String>();
        Query query = new SimpleQuery(super.query);
        Result describleResult = metaSearchManager.describe(query, null);

        for (String resource : resources) {
            for (Result result : describleResult.getChildren()) {
                if (result.getChild(resource) != null) {
                    engineToRun.add(result.getId());
                    break;
                }
            }
        }
        return super.doSearch(engineToRun);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        super.setup(resolver, objectModel, src, par);
        if (null == this.rsrcs) {
            throw new IllegalArgumentException("null resources");
        }
        this.resources = new HashSet<String>();
        for (String element : this.rsrcs) {
            this.resources.add(element);
        }
    }
}
