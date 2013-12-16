package edu.stanford.irt.laneweb.search;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchable;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.spring.SearchCacheManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private AbstractXmlApplicationContext context;

    private HttpClient httpClient;

    private MetaSearchable<Result> manager;

    private SearchCacheManager searchCacheManager;

    @SuppressWarnings("unchecked")
    public MetaSearchManagerSource(final String springFileName) {
        this.context = new ClassPathXmlApplicationContext(springFileName);
        this.manager = this.context.getBean("manager", MetaSearchable.class);
        this.searchCacheManager = this.context.getBean("searchCacheManager", SearchCacheManager.class);
        this.httpClient = this.context.getBean("httpClient", HttpClient.class);
    }

    public void dispose() {
        this.context.destroy();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public MetaSearchable<Result> getMetaSearchManager() {
        return this.manager;
    }

    public SearchCacheManager getSearchCacheManager() {
        return this.searchCacheManager;
    }

    @SuppressWarnings("unchecked")
    public void reload(final String url, final String login, final String password) throws IOException {
        AbstractXmlApplicationContext newContext = new HttpApplicationContext(url, login, password);
        this.manager = newContext.getBean("manager", MetaSearchable.class);
        this.httpClient = newContext.getBean("httpClient", HttpClient.class);
        this.searchCacheManager = newContext.getBean("searchCacheManager", SearchCacheManager.class);
        this.context.destroy();
        this.context = newContext;
    }
}