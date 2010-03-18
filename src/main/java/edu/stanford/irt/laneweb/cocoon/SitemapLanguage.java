package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;


public class SitemapLanguage extends org.apache.cocoon.components.treeprocessor.sitemap.SitemapLanguage {

    public SitemapLanguage(Context context, ServiceManager serviceManager) throws ContextException, ServiceException {
        contextualize(context);
        service(serviceManager);
    }
}
