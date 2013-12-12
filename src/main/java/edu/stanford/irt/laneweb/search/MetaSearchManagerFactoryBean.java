package edu.stanford.irt.laneweb.search;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;

public class MetaSearchManagerFactoryBean implements FactoryBean<MetaSearchManager<Result>> {

    private MetaSearchManagerSource msms;

    public MetaSearchManagerFactoryBean(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    public MetaSearchManager<Result> getObject() {
        return this.msms.getMetaSearchManager();
    }

    public Class<?> getObjectType() {
        return MetaSearchManager.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
