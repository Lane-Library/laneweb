package edu.stanford.irt.laneweb.search;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.stanford.irt.search.MetaSearchManager;

/**
 * @author ceyates
 */
public class LanewebMetaSearchManagerSource implements ThreadSafe, Disposable,
        MetaSearchManagerSource {

    private MetaSearchManager manager;

    private HttpClient httpClient;

    private ClassPathXmlApplicationContext context;

    public LanewebMetaSearchManagerSource() {
        this.context = new ClassPathXmlApplicationContext(
                "spring/metasearch.xml");
        this.manager = (MetaSearchManager) this.context.getBean("manager");
        this.httpClient = (HttpClient) this.context.getBean("httpClient");
    }

    public MetaSearchManager getMetaSearchManager() {
        return this.manager;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public void dispose() {
        this.context.destroy();
    }
}
