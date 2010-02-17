package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * 
 * @author ceyates
 *
 * $Id$
 */
public class DescribeGenerator extends AbstractSearchGenerator {
    
    private static final String[] NO_ENGINES = new String[0];

    private String[] engines;

    @Override //because query might be null which throws an exception in the parent class.
    public void initialize() {
        this.query = this.parameterMap.get(LanewebObjectModel.QUERY);
        this.engines = this.model.getObject("engines", String[].class, NO_ENGINES);
    }

    @Override
    protected Result doSearch() {
        Collection<String> engines = null;
        if ((this.engines != null) && (this.engines.length > 0)) {
            engines = new LinkedList<String>();
            for (String element : this.engines) {
                engines.add(element);
            }
        }
        if (this.query != null) {
            return this.metaSearchManager.describe(new SimpleQuery(this.query), engines);
        } else {
            return this.metaSearchManager.describe(new SimpleQuery(""), engines);
        }
    }
}
