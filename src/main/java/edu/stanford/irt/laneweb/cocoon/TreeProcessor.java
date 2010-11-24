package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.source.impl.DelayedRefreshSourceWrapper;
import org.apache.cocoon.environment.SourceResolver;


public class TreeProcessor extends org.apache.cocoon.components.treeprocessor.TreeProcessor {
    
    private String filename;

    public TreeProcessor(final String filename, final ServiceManager serviceManager, final Settings settings) {
        this.filename = filename;
        this.manager = serviceManager;
        this.settings = settings;
    }
    
    public void setSourceResolver(SourceResolver sourceResolver) {
        this.resolver = sourceResolver;
    }
    
    @Override
    public void initialize() throws Exception {
        this.source = new DelayedRefreshSourceWrapper(this.resolver.resolveURI(this.filename), this.lastModifiedDelay);
        super.initialize();
    }
}
