package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.Initializable;
import edu.stanford.irt.laneweb.cocoon.ModelAware;
import edu.stanford.irt.laneweb.cocoon.ParametersAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator implements ParametersAware, ModelAware, Initializable {

    private static final String[] NO_ENGINES = new String[0];

    private String[] engines;
    
    private Map<String, String> parameters;
    
    private Map<String, Object> model;
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public void setModel(Map<String, Object> model) {
        this.model = model;
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

    // because query might be null which throws an exception in the parent
    // class.
    public void initialize() {
        this.query = this.parameters.get(Model.QUERY);
        this.engines = ModelUtil.getObject(this.model, Model.ENGINES, String[].class, NO_ENGINES);
    }
}
