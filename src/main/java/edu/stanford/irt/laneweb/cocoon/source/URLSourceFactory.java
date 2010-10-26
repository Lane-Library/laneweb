package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;
import org.apache.excalibur.source.impl.URLSource;


public class URLSourceFactory implements SourceFactory {

    public Source getSource(String location, Map parameters) throws IOException, MalformedURLException {
        URL url = new URL(location);
        URLSource result = new URLSource();
        result.init(url, parameters);
        return result;
    }

    public void release(Source source) {
    }
}
