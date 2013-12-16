package edu.stanford.irt.laneweb.search;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.search.MetaSearchable;
import edu.stanford.irt.search.impl.Result;

public class MetaSearchManagerFactoryBean implements FactoryBean<MetaSearchable<Result>> {

    private MetaSearchManagerSource msms;

    public MetaSearchManagerFactoryBean(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    public MetaSearchable<Result> getObject() {
        return this.msms.getMetaSearchManager();
    }

    public Class<?> getObjectType() {
        return MetaSearchable.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
