package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.DefaultResult;

public class ResourceSearchGenerator extends SearchGenerator {

    private Collection<String> resources;

    @Override
    public Result doSearch() {
        Result allResult = super.doSearch(null);
        Result result = new DefaultResult(allResult.getId());
        synchronized (allResult) {
            result.setQuery(allResult.getQuery());
            result.setStatus(allResult.getStatus());
            Collection<Result> results = allResult.getChildren();
            for (Result engineResult : results) {
                if ((this.resources != null) && this.resources.contains(engineResult.getId())) {
                    result.addChild(engineResult);
                } else if (this.resources != null) {
                    Collection<Result> resourceResults = engineResult.getChildren();
                    for (Result resourceResult : resourceResults) {
                        if (this.resources.contains(resourceResult.getId())) {
                            result.addChild(engineResult);
                            break;
                        }
                    }
                }
            }
        }
        return result;
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
