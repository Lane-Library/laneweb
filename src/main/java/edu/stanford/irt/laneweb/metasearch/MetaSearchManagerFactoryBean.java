package edu.stanford.irt.laneweb.metasearch;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.search.impl.MetaSearchManager;

public class MetaSearchManagerFactoryBean implements FactoryBean<MetaSearchManager> {

    private MetaSearchManagerSource msms;

    public MetaSearchManagerFactoryBean(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    @Override
    public MetaSearchManager getObject() {
        return this.msms.getMetaSearchManager();
    }

    @Override
    public Class<MetaSearchManager> getObjectType() {
        return MetaSearchManager.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
