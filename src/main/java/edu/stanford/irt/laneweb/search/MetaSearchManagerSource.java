package edu.stanford.irt.laneweb.search;

import org.apache.http.client.HttpClient;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.spring.SearchCacheManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private AbstractXmlApplicationContext context;

    private HttpClient httpClient;

    private MetaSearchManager manager;

    private SearchCacheManager searchCacheManager;

    public MetaSearchManagerSource(final String springFileName) {
        this.context = new ClassPathXmlApplicationContext(springFileName);
        this.manager = this.context.getBean("manager", MetaSearchManager.class);
        this.searchCacheManager = this.context.getBean("searchCacheManager", SearchCacheManager.class);
        this.httpClient = this.context.getBean("httpClient", HttpClient.class);
    }

    public void dispose() {
        this.context.destroy();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public MetaSearchManager getMetaSearchManager() {
        return this.manager;
    }

    public SearchCacheManager getSearchCacheManager() {
        return this.searchCacheManager;
    }
}