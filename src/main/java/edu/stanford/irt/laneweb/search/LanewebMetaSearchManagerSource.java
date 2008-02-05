package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.MetaSearchManager;


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
	
	
	public LanewebMetaSearchManagerSource()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/metasearch.xml");
		this.manager = (MetaSearchManager) context.getBean("manager");
	}
	
	
	public MetaSearchManager getMetaSearchManager() {
		return  this.manager;
	}
	
		

}
