package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;

public abstract class AbstractSearchGenerator extends AbstractGenerator {

    protected String query;
    
    protected int show;

    @Override
    protected void initialize() {
        this.query = getString(this.model, Model.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
        String show = getString(this.model, Model.SHOW, "0");
        this.show = "all".equals(show) ? -1 : Integer.parseInt(show);
    }
}