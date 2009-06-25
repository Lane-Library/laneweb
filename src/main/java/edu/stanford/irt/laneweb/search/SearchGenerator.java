package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.lane.icd9.ICD9Translator;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.CachedMetaSearchManagerImpl;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

/**
 * @author ceyates
 */
public class SearchGenerator implements Generator {

    private String clearCache;

    private long defaultTimeout;

    private String[] e;

    private ICD9Translator icd9Translator;

    private MetaSearchManager metaSearchManager;

    private String q;

    private String[] r;

    private String t;

    private String w;

    private String s;
    
    private XMLConsumer xmlConsumer;

    public void generate() throws IOException, SAXException, ProcessingException {
        if ("y".equalsIgnoreCase(this.clearCache)) {
            ((CachedMetaSearchManagerImpl) this.metaSearchManager).clearCache(this.q);
        }
        if ("all".equalsIgnoreCase(this.clearCache)) {
            ((CachedMetaSearchManagerImpl) this.metaSearchManager).clearAllCaches();
        }
        Result result = null;
        Collection<String> engines = null;
        Collection<String> resources = null;
        if ((this.e != null) && (this.e.length > 0)) {
            engines = new HashSet<String>();
            for (String element : this.e) {
                engines.add(element);
            }
        }
        if ((this.r != null) && (this.r.length > 0)) {
            resources = new HashSet<String>();
            for (String element : this.r) {
                resources.add(element);
            }
        }
        if ((this.q != null) && (this.q.length() > 0)) {
            long time = this.defaultTimeout;
            if (null != this.t) {
                try {
                    time = Long.parseLong(this.t);
                } catch (NumberFormatException nfe) {
                    ;
                }
            }
            long timeout = time;
            String queryString = this.q;
            if (this.icd9Translator.isICD9Code(this.q)) {
                queryString = this.icd9Translator.getLongName(this.q);
            }
            final SimpleQuery query = new SimpleQuery(queryString);
            boolean synchronous = false;
            if ((this.s != null) && (this.s.length() > 0)) {
              synchronous = Boolean.parseBoolean(this.s);
            }
            result = this.metaSearchManager.search(query, timeout, engines, synchronous);
            if (null != this.w) {
                long wait = 0;
                try {
                    wait = Long.parseLong(this.w);
                } catch (NumberFormatException nfe) {
                    ;
                }
                long start = System.currentTimeMillis();
                try {
                    synchronized (result) {
                        while ((wait > 0) && SearchStatus.RUNNING.equals(result.getStatus())) {
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
        } else if ((engines != null) || (resources != null)) {
            Result limitedResult = new DefaultResult(result.getId());
            limitedResult.setQuery(result.getQuery());
            limitedResult.setStatus(result.getStatus());
            Collection<Result> selectedResult = new ArrayList<Result>();
            synchronized (result) {
                Collection<Result> results = result.getChildren();
                for (Result engineResult : results) {
                    if ((engines != null) && engines.contains(engineResult.getId())) {
                        selectedResult.add(engineResult);
                    } else if (resources != null) {
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
        }
        SAXable xml = new SAXResult(result);
        synchronized (result) {
            xml.toSAX(this.xmlConsumer);
        }
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setICD9Translator(final ICD9Translator icd9Translator) {
        this.icd9Translator = icd9Translator;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) throws ProcessingException, SAXException,
            IOException {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        this.q = request.getParameter("q");
        this.t = request.getParameter("t");
        if(null == this.t){
          this.t = par.getParameter("t", null);
        }
        this.e = request.getParameterValues("e");
        this.w = request.getParameter("w");
        this.r = request.getParameterValues("r");
        this.s = request.getParameter("s");
        if(null == this.s){
          this.s = par.getParameter("s", null);
        }
        this.clearCache = request.getParameter("clearcache");
    }
}
