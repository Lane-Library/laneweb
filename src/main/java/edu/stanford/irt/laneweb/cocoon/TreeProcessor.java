package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.source.impl.DelayedRefreshSourceWrapper;

public class TreeProcessor extends org.apache.cocoon.components.treeprocessor.TreeProcessor {

    public TreeProcessor(final String fileName, final SourceResolver sourceResolver,
            final ServiceManager serviceManager, final Settings settings) throws Exception {
        this.resolver = sourceResolver;
        this.manager = serviceManager;
        this.settings = settings;
        this.source = new DelayedRefreshSourceWrapper(this.resolver.resolveURI(fileName), this.lastModifiedDelay);
        initialize();
    }
}
