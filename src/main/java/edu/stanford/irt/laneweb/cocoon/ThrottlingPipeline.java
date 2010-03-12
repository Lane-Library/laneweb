package edu.stanford.irt.laneweb.cocoon;

import java.util.Collection;
import java.util.HashSet;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.impl.NonCachingProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ThrottlingPipeline
 */
public class ThrottlingPipeline extends NonCachingProcessingPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThrottlingPipeline.class);

    /**
     * the currently processed host/urls. The superclass is a pooled Component so this has to be a static variable.
     */
    private static final Collection<String> REQUESTS = new HashSet<String>();

    private String requestKey;

    @Override
    public void recycle() {
        super.recycle();
        this.requestKey = null;
    }

    @Override
    public void setup(final Parameters params) {
        this.requestKey = params.getParameter("request-key", null);
    }

    /**
     * Process the given <code>Environment</code>, producing the output. I created this to keep Denial of Service
     * attacks to the eresources urls. from bringing down the site. Only one request for a given url from a given client
     * IP is processed at one time. Others will throw a ProcessingException
     */
    @Override
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        if (null == this.requestKey) {
            throw new IllegalStateException("null requestKey");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("requestKey = " + this.requestKey);
        }
        synchronized (REQUESTS) {
            if (REQUESTS.contains(this.requestKey)) {
                throw new ProcessingException("too many concurrent requests. key:" + this.requestKey);
            }
            REQUESTS.add(this.requestKey);
        }
        try {
            return super.processXMLPipeline(environment);
        } finally {
            synchronized (REQUESTS) {
                REQUESTS.remove(this.requestKey);
            }
        }
    }
}
