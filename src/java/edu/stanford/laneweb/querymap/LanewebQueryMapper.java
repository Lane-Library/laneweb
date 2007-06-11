/**
 * 
 */
package edu.stanford.laneweb.querymap;

import java.util.HashSet;
import java.util.Set;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import edu.stanford.irt.querymap.ClasspathDescriptorToResource;
import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryToDescriptor;
import edu.stanford.irt.querymap.ResourceMap;

/**
 * @author ceyates
 *
 */
public class LanewebQueryMapper extends AbstractLogEnabled implements QueryMapper, Configurable, Initializable, ThreadSafe {
	
	private edu.stanford.irt.querymap.QueryMapper queryMapper;
	
	private Set<String> stopDescriptors;
	
	public LanewebQueryMapper() {
	}
	
	public QueryMap getQueryMap(String query) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("getResourceMap("+query+")");
		}
		QueryMap queryMap = this.queryMapper.getQueryMap(query);
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("resourceMap = " + (queryMap == null ? "null" : queryMap.toString()));
		}
		return queryMap;
	}

	public void configure(Configuration config) throws ConfigurationException {
		Configuration[] configs = config.getChildren();
		this.stopDescriptors = new HashSet<String>(configs.length);
		for (int i = 0; i < configs.length; i++) {
			this.stopDescriptors.add(configs[i].getValue());
		}
	}

	public void initialize() throws Exception {
		this.queryMapper = new edu.stanford.irt.querymap.QueryMapper();
		DescriptorManager descriptorManager = new DescriptorManager();
		this.queryMapper.setDescriptorManager(descriptorManager);
		QueryToDescriptor queryToDescriptor = new QueryToDescriptor();
		queryToDescriptor.setDescriptorManager(descriptorManager);
		queryToDescriptor.setHttpClient(new HttpClient(new MultiThreadedHttpConnectionManager()));
		queryToDescriptor.setStopDescriptors(this.stopDescriptors);
		this.queryMapper.setQueryToDescriptor(queryToDescriptor);
		this.queryMapper.setDescriptorToResource(new ClasspathDescriptorToResource());
	}

}
