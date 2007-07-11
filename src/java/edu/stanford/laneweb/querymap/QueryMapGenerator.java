package edu.stanford.laneweb.querymap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.excalibur.source.Source;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.DescriptorToResource;
import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryToDescriptor;
import edu.stanford.irt.querymap.ResourceMap;
import edu.stanford.irt.querymap.StreamResourceMapping;

public class QueryMapGenerator extends ServiceableGenerator {
	
	private static final String QUERY_MAP = "query-map";
	private static final String QUERY = "query";
	private static final String DESCRIPTOR = "descriptor";
	private static final String RESOURCE_MAP = "resource-map";
	private static final String RESOURCE = "resource";
	private static final String IDREF = "idref";
	private static final String NAMESPACE = "http://lane.stanford.edu/querymap/ns";
	
	private DescriptorManager descriptorManager;
	private edu.stanford.irt.querymap.QueryMapper queryMapper;
	private HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
	
	private String query;

	public void service(ServiceManager serviceManager) throws ServiceException {
		super.service(serviceManager);
		DescriptorManagerManager managerManager = (DescriptorManagerManager) serviceManager.lookup(DescriptorManagerManager.ROLE);
		this.descriptorManager = managerManager.getDescriptorManager();
		serviceManager.release(managerManager);
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params) throws ProcessingException, SAXException, IOException {
		super.setup(resolver, objectModel, src, params);
		String resourceMap = params.getParameter("resource-maps", "resource://edu/stanford/irt/querymap/resource-maps.xml");
		String descriptorWeights = params.getParameter("descriptor-weights","resource://edu/stanford/irt/querymap/descriptor-weights.xml");
		int abstractCount = params.getParameterAsInteger("abstract-count",100);
		this.query = params.getParameter(QUERY, null);
		this.queryMapper = new edu.stanford.irt.querymap.QueryMapper();
		this.queryMapper.setDescriptorManager(this.descriptorManager);
		QueryToDescriptor queryToDescriptor = new QueryToDescriptor();
		queryToDescriptor.setDescriptorManager(this.descriptorManager);
		queryToDescriptor.setHttpClient(this.httpClient);
		this.queryMapper.setQueryToDescriptor(queryToDescriptor);
		Source resourceMapSource = resolver.resolveURI(resourceMap);
		InputStream inputStream = resourceMapSource.getInputStream();
		StreamResourceMapping resourceMapping = new StreamResourceMapping(inputStream);
		resolver.release(resourceMapSource);
		Source descriptorWeightSource = resolver.resolveURI(descriptorWeights);
		DescriptorWeightMap descriptorWeightMap = new DescriptorWeightMap(descriptorWeightSource.getInputStream());
		resolver.release(descriptorWeightSource);
		queryToDescriptor.setDescriptorWeights(descriptorWeightMap);
		queryToDescriptor.setAbstractCount(abstractCount);
		DescriptorToResource descriptorToResource = new DescriptorToResource();
		descriptorToResource.setResourceMap(resourceMapping);
		this.queryMapper.setDescriptorToResource(descriptorToResource);
	}

	public void generate() throws IOException, SAXException,
			ProcessingException {
		QueryMap queryMap = null;
		if (null != this.query) {
			queryMap = this.queryMapper.getQueryMap(this.query);
		}
		this.contentHandler.startDocument();
		XMLUtils.startElement(this.contentHandler, NAMESPACE, QUERY_MAP);
		XMLUtils.createElementNS(this.contentHandler, NAMESPACE, QUERY, queryMap.getQuery());
		Descriptor descriptor = queryMap.getDescriptor();
		if (null != descriptor) {
			XMLUtils.createElementNS(this.contentHandler, NAMESPACE, DESCRIPTOR, descriptor.getDescriptorName());
			ResourceMap resourceMap = queryMap.getResourceMap();
			if (null != resourceMap) {
				XMLUtils.startElement(this.contentHandler, NAMESPACE, RESOURCE_MAP);
				XMLUtils.createElementNS(this.contentHandler, NAMESPACE, DESCRIPTOR,resourceMap.getDescriptor().getDescriptorName());
				for (String idref : resourceMap.getResources()) {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute("", IDREF, IDREF, "IDREF", idref);
					XMLUtils.createElementNS(this.contentHandler, NAMESPACE, RESOURCE, atts);
				}
				XMLUtils.endElement(this.contentHandler, NAMESPACE, RESOURCE_MAP);
			}
		}
		XMLUtils.endElement(this.contentHandler, NAMESPACE, QUERY_MAP);
		this.contentHandler.endDocument();
	}
	
	@Override
	public void recycle() {
		this.query = null;
		this.queryMapper = null;
		super.recycle();
	}

}
