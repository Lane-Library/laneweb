package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ceyates
 */
public class SearchGenerator extends AbstractSearchGenerator implements Generator {


    private long defaultTimeout;

    private MetaSearchManager metaSearchManager;

    private String t;

    private String w;

    private String s;
    
    public Result doSearch() {
        return doSearch(null);
    }
    
    public Result doSearch(Collection<String> engines){
        Result result = null;
        if ((this.query != null) && (this.query.length() > 0)) {
            long time = this.defaultTimeout;
            if (null != this.t) {
                try {
                    time = Long.parseLong(this.t);
                } catch (NumberFormatException nfe) {
                    ;
                }
            }
            long timeout = time;
            boolean synchronous = false;
            if ((this.s != null) && (this.s.length() > 0)) {
              synchronous = Boolean.parseBoolean(this.s);
            }
            final SimpleQuery query = new SimpleQuery(this.query);
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
        }
        return result;
    }

    
    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par){
           super.setup(resolver, objectModel, src, par);
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        this.t = request.getParameter("t");
        if(null == this.t){
          this.t = par.getParameter("t", null);
        }
        this.w = request.getParameter("w");
        this.s = request.getParameter("s");
        if(null == this.s){
          this.s = par.getParameter("s", null);
        }
    }
}
