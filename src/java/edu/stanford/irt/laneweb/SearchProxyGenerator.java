package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.commons.httpclient.HttpState;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.xml.sax.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.httpclient.HTTPClientSource;

public class SearchProxyGenerator extends ServiceableGenerator {
	
	private SAXParser parser;
	private Source src;

	@Override
	public void recycle() {
		this.resolver.release(this.src);
		this.manager.release(this.parser);
		this.source = null;
		this.parser = null;
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
	    String queryString = buildQuery(URLEncoder.encode(query,"UTF-8"), timeout, id, wait, engines);
        Session session = request.getSession(true);
        HttpState httpState = (HttpState) session.getAttribute(HTTPClientSource.HTTP_STATE);
        if (null == httpState) {
        	httpState = new HttpState();
        	session.setAttribute(HTTPClientSource.HTTP_STATE, httpState);
        }
        Map map = new HashMap();
        map.put(HTTPClientSource.HTTP_STATE, httpState);
        this.src = this.resolver.resolveURI(this.source + queryString,null,map);
        try {
			this.parser = (SAXParser) this.manager.lookup(SAXParser.ROLE);
		} catch (ServiceException e) {
			throw new ProcessingException(e);
		}
	}
	
	public void generate() throws IOException, SAXException,
			ProcessingException {
		InputSource inputSource = new InputSource(this.src.getInputStream());
		this.parser.parse(inputSource, this.xmlConsumer);
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
