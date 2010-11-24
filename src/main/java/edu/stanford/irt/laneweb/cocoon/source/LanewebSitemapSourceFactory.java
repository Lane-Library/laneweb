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
        String modifiedLocation = location;
        if (modifiedLocation.indexOf("cocoon://") == 0) {
            modifiedLocation = "cocoon:/" + modifiedLocation.substring("cocoon://".length());
        }
        if (modifiedLocation.indexOf(".html") > 0 && modifiedLocation.indexOf("cocoon:/content") != 0) {
            modifiedLocation = "cocoon:/content/" + modifiedLocation.substring("cocoon:/".length());
        }
        return new LanewebSitemapSource(modifiedLocation, this.processor);
    }

    public void release(final Source source) {
    }
    
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
