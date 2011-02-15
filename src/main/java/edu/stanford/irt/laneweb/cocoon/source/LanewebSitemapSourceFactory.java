package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.util.Map;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

public abstract class LanewebSitemapSourceFactory implements SourceFactory {

    private Processor processor;

    @SuppressWarnings("rawtypes")
    public Source getSource(final String location, final Map parameters) throws IOException {
        return new LanewebSitemapSource(location, getEnvironment(), this.processor);
    }

    public void release(final Source source) {
    }

    public void setProcessor(final Processor processor) {
        this.processor = processor;
    }
    
    protected abstract Environment getEnvironment();
}
