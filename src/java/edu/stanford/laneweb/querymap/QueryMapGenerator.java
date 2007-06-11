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
			this.query = params.getParameter("query");
		} catch (ParameterException e) {
			throw new ProcessingException(e);
		}
	}

	public void generate() throws IOException, SAXException,
			ProcessingException {
		try {
		QueryMap queryMap = null;
		if (null != this.query) {
			queryMap = this.queryMapper.getQueryMap(this.query);
		}
		this.contentHandler.startDocument();
		XMLUtils.startElement(this.contentHandler, "query-map");
		XMLUtils.createElement(this.contentHandler, "query", queryMap.getQuery());
		Descriptor descriptor = queryMap.getDescriptor();
		if (null != descriptor) {
			XMLUtils.createElement(this.contentHandler, "descriptor", descriptor.getDescriptorName());
			ResourceMap resourceMap = queryMap.getResourceMap();
			if (null != resourceMap) {
				XMLUtils.startElement(this.contentHandler, "resource-map");
				XMLUtils.createElement(this.contentHandler, "descriptor",resourceMap.getDescriptor().getDescriptorName());
				for (String idref : resourceMap.getResources()) {
					AttributesImpl atts = new AttributesImpl();
					atts.addAttribute("", "idref", "idref", "IDREF", idref);
					XMLUtils.createElement(this.contentHandler, "resource", atts);
				}
				XMLUtils.endElement(this.contentHandler, "resource-map");
			}
		}
		XMLUtils.endElement(this.contentHandler, "query-map");
		this.contentHandler.endDocument();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void recycle() {
		this.query = null;
		super.recycle();
	}

}
