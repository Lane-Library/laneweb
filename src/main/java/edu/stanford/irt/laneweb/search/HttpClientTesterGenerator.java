package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.SAXException;

public class HttpClientTesterGenerator extends ServiceableGenerator{

	private HttpClient httpClient;
	private String url; 
	
	
	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		MetaSearchManagerSource source = (MetaSearchManagerSource) this.manager.lookup(MetaSearchManagerSource.class
				.getName());
		this.httpClient = source.getHttpClient();
		
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException,
			SAXException, IOException {
		super.setup(resolver, objectModel, src, par);
		Request request = (Request) objectModel.get(ObjectModelHelper.REQUEST_OBJECT);
		url = request.getParameter("targetUrl");
	}

	@Override
	public void recycle() {
		super.recycle();
	}

	
	
	
	public void generate() throws IOException, SAXException, ProcessingException {
		
		if(url != null && !"".equals(url.trim()))
		{
			if(!url.startsWith("http"))
				url = "http://".concat(url);
			try
			{
				GetMethod get = new GetMethod(url);
				httpClient.executeMethod(get);
				get.getResponseBodyAsString();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
