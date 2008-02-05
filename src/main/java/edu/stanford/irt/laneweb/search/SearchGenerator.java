package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.CachedMetaSearchManagerImpl;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.xml.sax.SAXException;

/**
 * @author ceyates
 * 
 */
public class SearchGenerator extends ServiceableGenerator implements Parameterizable{

	private MetaSearchManager metaSearchManager;
	
	private long			  defaultTimeout;

	private String			q;

	private String			t;

	private String[]		  e;

	private String			w;

	private String[]		  r;

	private String			keywords;

	private String			cache;

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		MetaSearchManagerSource source = (MetaSearchManagerSource) this.manager.lookup(MetaSearchManagerSource.class
				.getName());
		this.metaSearchManager = (CachedMetaSearchManagerImpl)source.getMetaSearchManager();
		
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException,
			SAXException, IOException {
		super.setup(resolver, objectModel, src, par);
		Request request = (Request) objectModel.get(ObjectModelHelper.REQUEST_OBJECT);
		this.q = request.getParameter("q");
		this.t = request.getParameter("t");
		this.e = request.getParameterValues("e");
		this.w = request.getParameter("w");
		this.r = request.getParameterValues("r");
		this.keywords = request.getParameter("keywords");
		this.cache = request.getParameter("clearcache");
	}

	@Override
	public void recycle() {
		this.q = null;
		this.t = null;
		this.e = null;
		this.w = null;
		this.r = null;
		this.keywords = null;
		this.cache = null;
		super.recycle();
	}
	
	public void parameterize(Parameters param) throws ParameterException {
		this.defaultTimeout = param.getParameterAsLong("defaultTimeout");
		
	}


	public void generate() throws IOException, SAXException, ProcessingException {

		if (q == null && keywords != null) {
			q = keywords;
		}

		if ("y".equalsIgnoreCase(cache))
			((CachedMetaSearchManagerImpl)metaSearchManager).clearCache(keywords);
		if("all".equalsIgnoreCase(cache))
			((CachedMetaSearchManagerImpl) metaSearchManager).clearAllCaches();

		Result result = null;

		Collection<String> engines = null;
		Collection<String> resources = null;

		if (e != null && e.length > 0) {
			engines = new HashSet<String>();
			for (String element : e) {
				engines.add(element);
			}
		}

		if (r != null && r.length > 0) {
			resources = new HashSet<String>();
			for (String element : r) {
				resources.add(element);
			}
		}

		if (result == null && q != null && q.length() > 0) {

			long time = this.defaultTimeout;
			if (null != t) {
				try {
					time = Long.parseLong(t);
				} catch (NumberFormatException nfe) {
					;
				}
			}
			long timeout = time;

			final SimpleQuery query = new SimpleQuery(q);
			result = this.metaSearchManager.search(query, timeout, engines, false);

			if (null != w) {
				long wait = 0;
				try {
					wait = Long.parseLong(w);
				} catch (NumberFormatException nfe) {
					;
				}

				long start = System.currentTimeMillis();

				try {
					synchronized (result) {
						while (wait > 0 && SearchStatus.RUNNING.equals(result.getStatus())) {
							result.wait(wait);
							if (wait != 0) {
								long now = System.currentTimeMillis();
								wait = wait - (now - start);
								start = now;
							}
						}
					}
				} catch (InterruptedException ie) {
					throw new RuntimeException(ie);
				}
			}

		}

		if (result == null) {
			result = new DefaultResult("null");
			result.setStatus(SearchStatus.FAILED);
		} else if (engines != null || resources != null) {
			Result limitedResult = new DefaultResult(result.getId());
			limitedResult.setQuery(result.getQuery());
			limitedResult.setStatus(result.getStatus());
			Collection<Result> selectedResult = new ArrayList<Result>();
			Collection<Result> results = result.getChildren();
			for (Result engineResult : results) {
				if (engines != null && engines.contains(engineResult.getId()))
					selectedResult.add(engineResult);
				else if (resources != null) {
					Collection<Result> resourceResults = engineResult.getChildren();
					for (Result resourceResult : resourceResults) {
						if (resources.contains(resourceResult.getId())) {
							selectedResult.add(engineResult);
							break;
						}
					}
				}
			}
			limitedResult.setChildren(selectedResult);
			result = limitedResult;
		}
		SAXable xml = new SAXResult(result);
	        synchronized(result) {
	       xml.toSAX(this.xmlConsumer);
	    }
	}
	
	
	
}
