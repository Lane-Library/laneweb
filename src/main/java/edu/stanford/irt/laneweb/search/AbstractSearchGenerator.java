package edu.stanford.irt.laneweb.search;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractSearchGenerator extends AbstractGenerator implements ModelAware {

    protected int page;

    protected String query;

    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
        String page = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
    }
}