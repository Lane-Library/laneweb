package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.sitemap.DefaultContentAggregator;


public class ContentAggregator extends DefaultContentAggregator {
    
    public ContentAggregator(ServiceManager serviceManager) {
        this.manager = serviceManager;
    }
}
