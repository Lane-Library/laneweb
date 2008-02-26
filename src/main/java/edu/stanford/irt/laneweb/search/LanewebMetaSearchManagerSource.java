
package edu.stanford.irt.laneweb.search;

import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchManager;

/**
 * @author ceyates
 * 
 */
public class LanewebMetaSearchManagerSource implements ThreadSafe, MetaSearchManagerSource {

    private MetaSearchManager manager;
    private HttpClient httpClient;
    
    public LanewebMetaSearchManagerSource() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
        this.manager = (MetaSearchManager) context.getBean("manager");
        this.httpClient = (HttpClient) context.getBean("httpClient");
    }

    public MetaSearchManager getMetaSearchManager() {
        return this.manager;
    }
    public HttpClient getHttpClient() {
        return this.httpClient;
    }
}
