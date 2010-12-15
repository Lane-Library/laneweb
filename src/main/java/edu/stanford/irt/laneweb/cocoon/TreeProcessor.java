package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.source.impl.DelayedRefreshSourceWrapper;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;


public class TreeProcessor extends org.apache.cocoon.components.treeprocessor.TreeProcessor {
    
    @Override
    public boolean process(Environment environment) throws Exception {
        if (this.source == null) {
            this.source = new DelayedRefreshSourceWrapper(this.resolver.resolveURI(this.filename), this.lastModifiedDelay);
            super.initialize();
        }
        return super.process(environment);
    }

    @Override
    public InternalPipelineDescription buildPipeline(Environment environment) throws Exception {
        if (this.source == null) {
            this.source = new DelayedRefreshSourceWrapper(this.resolver.resolveURI(this.filename), this.lastModifiedDelay);
            super.initialize();
        }
        return super.buildPipeline(environment);
    }

    private String filename;

    public TreeProcessor(final String filename, final ServiceManager serviceManager, final Settings settings) {
        this.filename = filename;
        this.manager = serviceManager;
        this.settings = settings;
    }
    
    public void setSourceResolver(SourceResolver sourceResolver) {
        this.resolver = sourceResolver;
    }
}
