package edu.stanford.irt.laneweb.search;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.search.legacy.LegacyMetaSearch;

public class MetaSearchManagerFactoryBean implements FactoryBean<LegacyMetaSearch> {

    private MetaSearchManagerSource msms;

    public MetaSearchManagerFactoryBean(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    public LegacyMetaSearch getObject() {
        return this.msms.getMetaSearchManager();
    }

    public Class<?> getObjectType() {
        return LegacyMetaSearch.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
