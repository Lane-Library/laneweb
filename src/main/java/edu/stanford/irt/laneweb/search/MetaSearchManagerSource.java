package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.LegacySearchable;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.spring.SearchCacheManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private static final class LoggingManager extends MetaSearchManager {

        private Logger log;

        private MetaSearchManager manager;

        public LoggingManager(final MetaSearchManager manager, final Logger log) {
            this.manager = manager;
            this.log = log;
        }

        @Override
        public Result describe(final Query query) {
            return this.manager.describe(query);
        }

        @Override
        public void dispose() {
            this.manager.dispose();
        }

        @Override
        public Result search(final Query query, final long timeout, final boolean synchronous) {
            Result r = this.manager.search(query, timeout, synchronous);
            maybeLogException(r);
            for (Result s : r.getChildren()) {
                maybeLogException(s);
                for (Result t : s.getChildren()) {
                    maybeLogException(t);
                    for (Result u : t.getChildren()) {
                        maybeLogException(u);
                        for (Result v : u.getChildren()) {
                            maybeLogException(v);
                        }
                    }
                }
            }
            return r;
        }

        @Override
        public void setSearchables(final Collection<LegacySearchable> searchables) {
            this.manager.setSearchables(searchables);
        }

        private void maybeLogException(final Result r) {
            Exception e = r.getException();
            if (e != null) {
                this.log.error(e.getMessage(), e);
            }
        }
    }

    private AbstractXmlApplicationContext context;

    private HttpClient httpClient;

    private Logger log;

    private MetaSearchManager manager;

    private SearchCacheManager searchCacheManager;

    public MetaSearchManagerSource(final String springFileName, final Logger log) {
        this.context = new ClassPathXmlApplicationContext(springFileName);
        this.manager = new LoggingManager(this.context.getBean("manager", MetaSearchManager.class), log);
        this.searchCacheManager = this.context.getBean("searchCacheManager", SearchCacheManager.class);
        this.httpClient = this.context.getBean("httpClient", HttpClient.class);
        this.log = log;
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

    public void reload(final String url, final String login, final String password) throws IOException {
        AbstractXmlApplicationContext newContext = new HttpApplicationContext(url, login, password);
        this.manager = new LoggingManager(newContext.getBean("manager", MetaSearchManager.class), this.log);
        this.httpClient = newContext.getBean("httpClient", HttpClient.class);
        this.searchCacheManager = newContext.getBean("searchCacheManager", SearchCacheManager.class);
        this.context.destroy();
        this.context = newContext;
    }
}