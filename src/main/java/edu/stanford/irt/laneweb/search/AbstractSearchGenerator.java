package edu.stanford.irt.laneweb.search;

import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.cocoon.ModelAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractSearchGenerator extends AbstractGenerator implements ModelAware {

    protected int page;

    protected String query;

    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
        String page = ModelUtil.getString(model, Model.PAGE, "1");
        this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
    }
}