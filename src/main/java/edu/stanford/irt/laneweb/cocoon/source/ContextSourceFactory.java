package edu.stanford.irt.laneweb.cocoon.source;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

import edu.stanford.irt.laneweb.cocoon.SourceResolver;

public class ContextSourceFactory implements SourceFactory {

    private ServletContext servletContext;

    private SourceResolver sourceResolver;

    public ContextSourceFactory(final ServletContext servletContext, final SourceResolver sourceResolver) {
        this.servletContext = servletContext;
        this.sourceResolver = sourceResolver;
    }

    @SuppressWarnings("rawtypes")
    public Source getSource(final String location, final Map arg1) throws IOException, MalformedURLException {
        // Remove the protocol and the first '/'
        final int pos = location.indexOf(":/");
        final String path = location.substring(pos + 2);
        URL u;
        // Try to get a file first and fall back to a resource URL
        String actualPath = this.servletContext.getRealPath(path);
        if (actualPath != null) {
            u = new File(actualPath).toURI().toURL();
        } else {
            u = this.servletContext.getResource(path);
        }
        if (u != null) {
            Source source = this.sourceResolver.resolveURI(u.toExternalForm());
            return source;
        } else {
            throw new IllegalArgumentException(location + " could not be found.");
        }
    }

    public void release(final Source arg0) {
    }
}
