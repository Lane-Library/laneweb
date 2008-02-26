package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.MetaSearchManager;

import org.apache.commons.httpclient.HttpClient;

public interface MetaSearchManagerSource {

    public static final String ROLE = MetaSearchManagerSource.class.getName();

    MetaSearchManager getMetaSearchManager();
    HttpClient getHttpClient();

}
