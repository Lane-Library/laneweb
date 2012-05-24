package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

public class SearchGenerator extends AbstractMetasearchGenerator implements ParametersAware {

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

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
        this.wait = ModelUtil.getString(model, "wait");
        this.synchronous = ModelUtil.getString(model, Model.SYNCHRONOUS);
    }

    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
        if (this.synchronous == null) {
            this.synchronous = parameters.get(Model.SYNCHRONOUS);
        }
    }

    protected Result doSearch(final Collection<String> engines) {
        Result result = null;
        if ((this.query != null) && (this.query.length() > 0)) {
            long time = this.defaultTimeout;
            if (null != this.timeout) {
                try {
                    time = Long.parseLong(this.timeout);
                } catch (NumberFormatException nfe) {
                    time = this.defaultTimeout;
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
                    wait = 0;
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
}
