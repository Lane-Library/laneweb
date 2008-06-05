package edu.stanford.irt.laneweb.search;

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
 * 
 */
public class SearchGenerator extends ServiceableGenerator implements
        Parameterizable {

    private MetaSearchManager metaSearchManager;

    private long defaultTimeout;

    private String q;

    private String t;

    private String[] e;

    private String w;

    private String[] r;

    private String clearCache;

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        MetaSearchManagerSource source = (MetaSearchManagerSource) this.manager
                .lookup(MetaSearchManagerSource.class.getName());
        this.metaSearchManager = source.getMetaSearchManager();

    }

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = (Request) objectModel
                .get(ObjectModelHelper.REQUEST_OBJECT);
        this.q = request.getParameter("q");
        this.t = request.getParameter("t");
        this.e = request.getParameterValues("e");
        this.w = request.getParameter("w");
        this.r = request.getParameterValues("r");
        this.clearCache = request.getParameter("clearcache");
    }

    @Override
    public void recycle() {
        this.q = null;
        this.t = null;
        this.e = null;
        this.w = null;
        this.r = null;
        this.clearCache = null;
        super.recycle();
    }

    public void parameterize(final Parameters param) throws ParameterException {
        this.defaultTimeout = param.getParameterAsLong("defaultTimeout");

    }

    public void generate() throws IOException, SAXException,
            ProcessingException {

        if ("y".equalsIgnoreCase(this.clearCache)) {
            ((CachedMetaSearchManagerImpl) this.metaSearchManager)
                    .clearCache(this.q);
        }
        if ("all".equalsIgnoreCase(this.clearCache)) {
            ((CachedMetaSearchManagerImpl) this.metaSearchManager)
                    .clearAllCaches();
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

            final SimpleQuery query = new SimpleQuery(this.q);
            result = this.metaSearchManager.search(query, timeout, engines,
                    false);

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
                        while ((wait > 0)
                                && SearchStatus.RUNNING.equals(result
                                        .getStatus())) {
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
            Collection<Result> results = result.getChildren();
            for (Result engineResult : results) {
                if ((engines != null) && engines.contains(engineResult.getId())) {
                    selectedResult.add(engineResult);
                } else if (resources != null) {
                    Collection<Result> resourceResults = engineResult
                            .getChildren();
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
        synchronized (result) {
            xml.toSAX(this.xmlConsumer);
        }
    }

}
