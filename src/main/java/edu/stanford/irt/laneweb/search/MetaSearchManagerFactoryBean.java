package edu.stanford.irt.laneweb.search;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.impl.DefaultResult;

public class MetaSearchManagerFactoryBean implements FactoryBean<MetaSearchManager<DefaultResult>> {

    private MetaSearchManagerSource msms;

    public MetaSearchManagerFactoryBean(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    public MetaSearchManager<DefaultResult> getObject() {
        return this.msms.getMetaSearchManager();
    }

    public Class<?> getObjectType() {
        return MetaSearchManager.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
