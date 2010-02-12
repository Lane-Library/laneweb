package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;

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

    private String[] e;

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = par.getParameter("query", null);
        this.e = getObject("e", String[].class, NO_ENGINES);
    }

    @Override
    protected Result doSearch() {
        Collection<String> engines = null;
        if ((this.e != null) && (this.e.length > 0)) {
            engines = new LinkedList<String>();
            for (String element : this.e) {
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
