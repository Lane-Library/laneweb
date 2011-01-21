package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.util.Map;

import org.apache.cocoon.Processor;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

public class LanewebSitemapSourceFactory implements SourceFactory {

    private Processor processor;

    @SuppressWarnings("rawtypes")
    public Source getSource(final String location, final Map parameters) throws IOException {
        return new LanewebSitemapSource(location, this.processor);
    }

    public void release(final Source source) {
    }

    public void setProcessor(final Processor processor) {
        this.processor = processor;
    }
}
