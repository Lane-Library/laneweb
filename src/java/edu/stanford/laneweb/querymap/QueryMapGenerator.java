package edu.stanford.laneweb.querymap;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapGenerator extends ServiceableGenerator {
	
	private static final String QUERY_MAP = "query-map";
	private static final String QUERY = "query";
	private static final String DESCRIPTOR = "descriptor";
	private static final String RESOURCE_MAP = "resource-map";
	private static final String RESOURCE = "resource";
	private static final String IDREF = "idref";
	private static final String NAMESPACE = "http://lane.stanford.edu/querymap/ns";
	
	private QueryMapper queryMapper;
	
	private String query;

	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		this.queryMapper = (QueryMapper) manager.lookup(QueryMapper.ROLE);
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params) throws ProcessingException, SAXException, IOException {
		super.setup(resolver, objectModel, src, params);
		try {
			this.query = params.getParameter(QUERY);
		} catch (ParameterException e) {
			throw new ProcessingException(e);
		}
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
		super.recycle();
	}

}
