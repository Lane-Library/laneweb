package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.MetaSearchManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ceyates
 */
public class MetaSearchManagerSource {

    private Logger logger = Logger.getLogger(MetaSearchManagerSource.class);

    private MetaSearchManager manager;

    private AbstractXmlApplicationContext context;

    private HttpClient httpClient;

    public MetaSearchManagerSource() {
	this.context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
	this.manager = (MetaSearchManager) this.context.getBean("manager");
	this.httpClient = (HttpClient) this.context.getBean("httpClient");
    }

    public MetaSearchManager getMetaSearchManager() {
	return this.manager;
    }

    public void dispose() {
	this.context.destroy();
    }

    public HttpClient getHttpClient() {
	return this.httpClient;
    }

    public void reload(String url, String login, String password) {
	try {

	    AbstractXmlApplicationContext context = new HttpApplicationContext(url, login, password);
	    this.manager = (MetaSearchManager) context.getBean("manager");
	    this.httpClient = (HttpClient) context.getBean("httpClient");
	    this.context.destroy();
	    this.context = context;
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	}

    }

}