package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.source.impl.DelayedRefreshSourceWrapper;


public class TreeProcessor extends org.apache.cocoon.components.treeprocessor.TreeProcessor {
    
    public TreeProcessor(String fileName, SourceResolver sourceResolver, ServiceManager serviceManager, Settings settings) throws Exception {
        this.resolver = sourceResolver;
        this.manager = serviceManager;
        this.settings = settings;
        this.source = new DelayedRefreshSourceWrapper(this.resolver.resolveURI(fileName), lastModifiedDelay);
        initialize();
    }
}
