package edu.stanford.irt.laneweb.search;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractSearchGenerator<T> extends AbstractGenerator {

    private String query;

    private SAXStrategy<T> saxStrategy;

    protected AbstractSearchGenerator(final SAXStrategy<T> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (this.query == null || this.query.isEmpty()) {
            this.saxStrategy.toSAX(getEmptyResult(), xmlConsumer);
        } else {
            this.saxStrategy.toSAX(doSearch(this.query), xmlConsumer);
        }
    }

    protected abstract T doSearch(String query);

    protected abstract T getEmptyResult();
}
