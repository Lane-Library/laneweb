package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.logger.CommonsLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ResourceSourceFactory extends org.apache.excalibur.source.impl.ResourceSourceFactory {
    
    private static final Log LOGGER = LogFactory.getLog(ResourceSourceFactory.class);
    
    public ResourceSourceFactory() {
        enableLogging(new CommonsLogger(LOGGER, getClass().getName()));
    }
}
