package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;

public abstract class AbstractSearchGenerator extends AbstractGenerator {

    protected String query;

    @Override
    protected void initialize() {
        this.query = this.model.getString(Model.QUERY);
        this.query = PMIDNormalizer.normalize(this.query);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }
}