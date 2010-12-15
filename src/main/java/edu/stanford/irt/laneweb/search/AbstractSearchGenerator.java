package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractSearchGenerator extends AbstractGenerator {

    protected String query;
    
    protected int page;

    @Override
    protected void initialize() {
        this.query = ModelUtil.getString(this.model, Model.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
        String page = ModelUtil.getString(this.model, Model.PAGE, "1");
        this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
    }
}