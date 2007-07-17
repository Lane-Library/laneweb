package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.excalibur.xml.sax.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.httpclient.HttpClientManager;

public class SearchProxyGenerator extends ServiceableGenerator {
	
	private static final String HTTP_STATE = "httpState";
	
	private HttpClient httpClient;
	private HttpState httpState;
	private String queryString;
	private SAXParser parser;

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		HttpClientManager httpClientManager = (HttpClientManager) this.manager.lookup(HttpClientManager.ROLE);
		this.httpClient = httpClientManager.getHttpClient();
		this.manager.release(httpClientManager);
	}
	@Override
	public void dispose() {
		this.httpClient = null;
		super.dispose();
	}
	@Override
	public void recycle() {
		this.httpState = null;
		this.queryString = null;
		this.manager.release(this.parser);
		super.recycle();
	}
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		super.setup(resolver, objectModel, src, par);
		Request request = ObjectModelHelper.getRequest(this.objectModel);
	    String query = request.getParameter("q");
	    String timeout = request.getParameter("t");
	    String id = request.getParameter("id");
	    String[] engines = request.getParameterValues("e");
	    String wait = request.getParameter("w");
	    String keywords = request.getParameter("keywords");
	    if (null == query) {
	    	query = keywords;
	    }
	    this.queryString = buildQuery(query, timeout, id, wait, engines);
        Session session = request.getSession(true);
        this.httpState = (HttpState) session.getAttribute(HTTP_STATE);
        if (null == this.httpState) {
        	this.httpState = new HttpState();
        	session.setAttribute(HTTP_STATE, this.httpState);
        }
        try {
			this.parser = (SAXParser) this.manager.lookup(SAXParser.ROLE);
		} catch (ServiceException e) {
			throw new ProcessingException(e);
		}
	}
	
	public void generate() throws IOException, SAXException,
			ProcessingException {
		GetMethod get = new GetMethod(this.source + this.queryString);
		try {
			this.httpClient.executeMethod(null, get, this.httpState);
			InputStream input = get.getResponseBodyAsStream();
			this.parser.parse(new InputSource(input), this.xmlConsumer);
		} finally {
			get.releaseConnection();
		}
	}
	
	private String buildQuery(String query, String timeout, String id, String wait, String[] engines) {
        boolean needAmp = false;
        StringBuffer sb = new StringBuffer("?");
        if (null != query) {
        	sb.append("q=").append(query);
        	needAmp = true;
        }
        if (null != timeout) {
        	if (needAmp) {
        		sb.append('&');
        	}
        	sb.append("t=").append(timeout);
        	needAmp = true;
        }
        if (null != id) {
        	if (needAmp) {
        		sb.append('&');
        	}
        	sb.append("id=").append(id);
        	needAmp = true;
        }
        if (null != engines && engines.length > 0) {
        	for (int i = 0; i < engines.length; i++) {
            	if (needAmp) {
            		sb.append('&');
            	}
        		sb.append("e=").append(engines[i]);
        		needAmp = true;
        	}
        }
        if (null != wait) {
        	if (needAmp) {
        		sb.append('&');
        	}
        	sb.append("w=").append(wait);
        }
        return sb.toString();
	}
}
