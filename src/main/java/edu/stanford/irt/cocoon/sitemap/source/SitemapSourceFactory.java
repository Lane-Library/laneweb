package edu.stanford.irt.cocoon.sitemap.source;

import java.io.IOException;
import java.util.Map;

import org.apache.cocoon.Processor;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

public abstract class SitemapSourceFactory implements SourceFactory {

    private Processor processor;

    @SuppressWarnings("rawtypes")
    // TODO: should move a lot of the SitemapSource constructor here
    public Source getSource(final String location, final Map parameters) throws IOException {
        return new SitemapSource(location, getModel(), this.processor);
    }

    public void release(final Source source) {
    }

    public void setProcessor(final Processor processor) {
        this.processor = processor;
    }

    protected abstract Map<String, Object> getModel();
}
