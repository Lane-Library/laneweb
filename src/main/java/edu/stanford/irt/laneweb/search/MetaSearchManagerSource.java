package edu.stanford.irt.laneweb.search;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private AbstractXmlApplicationContext context;

    private HttpClient httpClient;

    private Logger logger = Logger.getLogger(MetaSearchManagerSource.class);

    private MetaSearchManager manager;

    public MetaSearchManagerSource() {
        this.context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
        this.manager = (MetaSearchManager) this.context.getBean("manager");
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

    public void reload(final String url, final String login, final String password) {
        try {
            AbstractXmlApplicationContext context = new HttpApplicationContext(url, login, password);
            this.manager = (MetaSearchManager) context.getBean("manager");
            this.httpClient = (HttpClient) context.getBean("httpClient");
            this.context.destroy();
            this.context = context;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }
}