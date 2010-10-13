package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ceyates $Id$
 */
public class SearchGenerator extends AbstractMetasearchGenerator {

    private long defaultTimeout;

    private String synchronous;

    private String timeout;

    private String wait;

    @Override
    public Result doSearch() {
        return doSearch(null);
    }

    public void setDefaultTimeout(final long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
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
                    throw new IllegalStateException(ie);
                }
            }
        }
        if (result == null) {
            result = new DefaultResult("null");
            result.setStatus(SearchStatus.FAILED);
        }
        return result;
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.timeout = getString(this.model, "timeout", this.parameterMap.get("timeout"));
        this.wait = getString(this.model, "wait");
        this.synchronous = getString(this.model, "synchronous", this.parameterMap.get("synchronous"));
    }
}
