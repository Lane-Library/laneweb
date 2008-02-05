package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.MetaSearchManager;

import net.sf.ehcache.CacheManager;

import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * @author ceyates
 *
 */
public class LanewebMetaSearchManagerSource implements ThreadSafe, MetaSearchManagerSource {

	private MetaSearchManager manager;
	private CacheManager cacheManager;
	private HttpClient httpClient;
	
	
	public LanewebMetaSearchManagerSource()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
		this.manager = (MetaSearchManager) context.getBean("manager");
		this.cacheManager = (CacheManager)context.getBean("cacheManager");
		this.httpClient = (HttpClient)context.getBean("httpClient");
	}
	
	
	public MetaSearchManager getMetaSearchManager() {
		return  this.manager;
	}
	
	public CacheManager getCacheManager()
	{
		return this.cacheManager;
	}
	
	public HttpClient geHttpClient()
	{
		return this.httpClient;
	}


	public HttpClient getHttpClient() {
		// TODO Auto-generated method stub
		return httpClient;
	}
	

}
