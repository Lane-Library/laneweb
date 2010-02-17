package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ceyates
 * $Id$
 */
public class SearchGenerator extends AbstractSearchGenerator {

    private long defaultTimeout;

    private String synchronous;

    private String timeout;

    private String wait;

    @Override
    public Result doSearch() {
        return doSearch(null);
    }

    protected Result doSearch(final Collection<String> engines) {
        Result result = null;
        if ((this.query != null) && (this.query.length() > 0)) {
            long time = this.defaultTimeout;
            if (null != this.timeout) {
                try {
                    time = Long.parseLong(this.timeout);
                } catch (NumberFormatException nfe) {
                    ;
                }
            }
            long timeout = time;
            boolean synchronous = false;
            if ((this.synchronous != null) && (this.synchronous.length() > 0)) {
                synchronous = Boolean.parseBoolean(this.synchronous);
            }
            final SimpleQuery query = new SimpleQuery(this.query);
            result = this.metaSearchManager.search(query, timeout, engines, synchronous);
            if (null != this.wait) {
                long wait = 0;
                try {
                    wait = Long.parseLong(this.wait);
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

    @Override
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        super.setup(resolver, objectModel, src, par);
        this.timeout = this.model.getString("timeout");
        if (null == this.timeout) {
            this.timeout = par.getParameter("timeout", null);
        }
        this.wait = this.model.getString("wait");
        this.synchronous = this.model.getString("synchronous");
        if (null == this.synchronous) {
            this.synchronous = par.getParameter("synchronous", null);
        }

    }
}
