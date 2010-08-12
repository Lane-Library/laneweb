package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;
import org.apache.excalibur.source.impl.ResourceSource;

public class ResourceSourceFactory implements SourceFactory {

    @SuppressWarnings("rawtypes")
    public Source getSource(final String location, final Map parameters) throws MalformedURLException, IOException,
            SourceException {
        return new ResourceSource(location);
    }

    public void release(final Source source) {
    }
}
