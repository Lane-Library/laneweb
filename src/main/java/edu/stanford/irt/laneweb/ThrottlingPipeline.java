package edu.stanford.irt.laneweb;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.impl.CachingProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.log4j.Logger;

/**
 * The ThrottlingPipeline
 */
public class ThrottlingPipeline extends CachingProcessingPipeline {

    /**
     * the currently processed host/urls. The superclass is a pooled Component
     * so this has to be a static variable.
     */
    private static Collection<String> REQUESTS = new HashSet<String>();

    private Logger logger = Logger.getLogger(ThrottlingPipeline.class);

    /**
     * Process the given <code>Environment</code>, producing the output. I
     * created this to keep Denial of Service attacks to the eresources urls.
     * from bringing down the site. Only one request for a given url from a
     * given client IP is processed at one time. Others will throw a
     * ProcessingException
     */
    @Override
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        HttpServletRequest request = ObjectModelHelper.getRequest(environment.getObjectModel());
        String requestKey = new StringBuffer(request.getRemoteAddr()).append(request.getRequestURI()).append("?").append(request.getQueryString()).toString();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("requestKey = " + requestKey);
        }
        synchronized (REQUESTS) {
            if (REQUESTS.contains(requestKey)) {
                throw new ProcessingException("too many concurrent requests");
            }
            REQUESTS.add(requestKey);
        }
        try {
            return super.processXMLPipeline(environment);
        } finally {
            synchronized (REQUESTS) {
                REQUESTS.remove(requestKey);
            }
        }
    }
}
