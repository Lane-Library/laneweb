package edu.stanford.irt.laneweb.metasearch;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.context.ConfigurableApplicationContext;

import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.spring.SearchCacheManager;

public class MetaSearchService {

    private ConfigurableApplicationContext context;

    private Object lock = new Object();

    public MetaSearchService(final ConfigurableApplicationContext context) {
        this.context = context;
    }

    public void clearAllCaches() {
        checkContext();
        this.context.getBean("searchCacheManager", SearchCacheManager.class).clearAllCaches();
    }

    public void clearCache(final String q) {
        checkContext();
        this.context.getBean("searchCacheManager", SearchCacheManager.class).clearCache(q);
    }

    public Result describe(final String query, final Collection<String> engines) {
        checkContext();
        return this.context.getBean("manager", MetaSearchManager.class).describe(new SimpleQuery(query), engines);
    }

    public void dispose() {
        synchronized (this.lock) {
            if (this.context.isActive()) {
                this.context.close();
            }
        }
        this.context.close();
    }

    public HttpResponse execute(final HttpGet httpGet) throws IOException {
        checkContext();
        return this.context.getBean("httpClient", HttpClient.class).execute(httpGet);
    }

    public Result search(final String query, final Collection<String> engines, final long wait) {
        checkContext();
        return this.context.getBean("manager", MetaSearchManager.class).search(new SimpleQuery(query), engines, wait);
    }

    private void checkContext() {
        synchronized (this.lock) {
            if (!this.context.isActive()) {
                this.context.refresh();
            }
        }
    }
}
