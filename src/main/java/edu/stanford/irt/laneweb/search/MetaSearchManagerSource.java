package edu.stanford.irt.laneweb.search;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.spring.SearchCacheManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private static final Logger LOGGER = Logger.getLogger(MetaSearchManagerSource.class);

    private AbstractXmlApplicationContext context;

    private HttpClient httpClient;

    private MetaSearchManager manager;

    private SearchCacheManager searchCacheManager;

    public MetaSearchManagerSource(final String springFileName) {
        this.context = new ClassPathXmlApplicationContext(springFileName);
        this.manager = (MetaSearchManager) this.context.getBean("manager");
        this.searchCacheManager = (SearchCacheManager) this.context.getBean("searchCacheManager");
        this.httpClient = (HttpClient) this.context.getBean("httpClient");
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

    public void reload(final String url, final String login, final String password) {
        try {
            AbstractXmlApplicationContext context = new HttpApplicationContext(url, login, password);
            this.manager = (MetaSearchManager) context.getBean("manager");
            this.httpClient = (HttpClient) context.getBean("httpClient");
            this.context.destroy();
            this.context = context;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}