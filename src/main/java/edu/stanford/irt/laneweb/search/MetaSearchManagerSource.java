package edu.stanford.irt.laneweb.search;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchManager;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private MetaSearchManager manager;

    private ClassPathXmlApplicationContext context;

    private HttpClient httpClient;	
    
    public MetaSearchManagerSource() {
        this.context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
        this.manager = (MetaSearchManager) this.context.getBean("manager");
        this.httpClient = (HttpClient)this.context.getBean("httpClient");
    }

    public MetaSearchManager getMetaSearchManager() {
        return this.manager;
    }

    public void dispose() {
        this.context.destroy();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
    
}