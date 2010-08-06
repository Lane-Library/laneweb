package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

public class SitemapSourceFactory implements SourceFactory {

    private org.apache.cocoon.components.source.impl.SitemapSourceFactory factory;

    public SitemapSourceFactory(final ServiceManager serviceManager) throws ServiceException {
        this.factory = new org.apache.cocoon.components.source.impl.SitemapSourceFactory();
        this.factory.service(serviceManager);
    }

    @SuppressWarnings("rawtypes")
    public Source getSource(final String location, final Map parameters) throws IOException {
        return this.factory.getSource(location, parameters);
    }

    public void release(final Source source) {
        this.factory.release(source);
    }
}
