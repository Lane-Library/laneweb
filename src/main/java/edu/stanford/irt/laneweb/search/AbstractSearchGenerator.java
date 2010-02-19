package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public abstract class AbstractSearchGenerator extends AbstractGenerator {

    protected String query;

    protected void initialize() {
        this.query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }
}