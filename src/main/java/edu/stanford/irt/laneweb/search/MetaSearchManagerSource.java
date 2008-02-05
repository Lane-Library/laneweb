package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.MetaSearchManager;

public interface MetaSearchManagerSource {

    public static final String ROLE = MetaSearchManagerSource.class.getName();

    MetaSearchManager getMetaSearchManager();
    
}
