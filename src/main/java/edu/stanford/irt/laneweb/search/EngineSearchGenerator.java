package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.StringTokenizer;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {
    
    private Collection<String> engines;

    @Override
    public Result doSearch() {
        return super.doSearch(this.engines);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.engines = this.model.getObject(LanewebObjectModel.ENGINES, Collection.class, Collections.<String>emptyList());
        if (this.engines.size() == 0) {
            String engineList = this.parameterMap.get("engine-list");
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList,","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }
}
