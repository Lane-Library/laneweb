package edu.stanford.irt.laneweb.search;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.search.MetaSearchManager;

public class MetaSearchManagerFactoryBean implements FactoryBean<MetaSearchManager> {

    private MetaSearchManagerSource msms;

    public MetaSearchManagerFactoryBean(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    public MetaSearchManager getObject() {
        return this.msms.getMetaSearchManager();
    }

    public Class<MetaSearchManager> getObjectType() {
        return MetaSearchManager.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
