/**
 * 
 */
package edu.stanford.laneweb.querymap;

import java.io.InputStream;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;

import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.DescriptorToResource;
import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryToDescriptor;
import edu.stanford.irt.querymap.StreamResourceMapping;

/**
 * @author ceyates
 *
 */
public class LanewebQueryMapper extends AbstractLogEnabled implements QueryMapper, Serviceable, Parameterizable, Initializable, ThreadSafe {
	
	private edu.stanford.irt.querymap.QueryMapper queryMapper;
	
	private String resourceMap;
	
	private String descriptorWeights;
	
	private ServiceManager manager;
	
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

	public void initialize() throws Exception {
		this.queryMapper = new edu.stanford.irt.querymap.QueryMapper();
		DescriptorManager descriptorManager = new DescriptorManager();
		this.queryMapper.setDescriptorManager(descriptorManager);
		QueryToDescriptor queryToDescriptor = new QueryToDescriptor();
		queryToDescriptor.setDescriptorManager(descriptorManager);
		queryToDescriptor.setHttpClient(new HttpClient(new MultiThreadedHttpConnectionManager()));
		this.queryMapper.setQueryToDescriptor(queryToDescriptor);
		SourceResolver resolver = (SourceResolver) this.manager.lookup(SourceResolver.ROLE);
		Source resourceMapSource = resolver.resolveURI(this.resourceMap);
		InputStream inputStream = resourceMapSource.getInputStream();
		StreamResourceMapping resourceMapping = new StreamResourceMapping(inputStream);
		resolver.release(resourceMapSource);
		Source descriptorWeightSource = resolver.resolveURI(this.descriptorWeights);
		DescriptorWeightMap descriptorWeights = new DescriptorWeightMap(descriptorWeightSource.getInputStream());
		resolver.release(descriptorWeightSource);
		queryToDescriptor.setDescriptorWeightAdjustments(descriptorWeights);
		this.manager.release(resolver);
		DescriptorToResource descriptorToResource = new DescriptorToResource();
		descriptorToResource.setResourceMap(resourceMapping);
		this.queryMapper.setDescriptorToResource(descriptorToResource);
	}

	/* (non-Javadoc)
	 * @see org.apache.avalon.framework.parameters.Parameterizable#parameterize(org.apache.avalon.framework.parameters.Parameters)
	 */
	public void parameterize(Parameters params) throws ParameterException {
		this.resourceMap = params.getParameter("resource-maps", "resource:/edu/stanford/irt/querymap/resource-maps.xml");
		this.descriptorWeights = params.getParameter("descriptor-weights","resource:/edu/stanford/irt/querymap/descriptor-weights.xml");
	}

	public void service(ServiceManager manager) throws ServiceException {
		this.manager = manager;
	}

}
