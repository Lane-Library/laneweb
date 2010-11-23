package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;
import org.apache.cocoon.environment.SourceResolver;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;


public class LanewebSourceResolver implements SourceResolver, ResourceLoaderAware {
    
    private Pattern TOMCAT_URL_PATTERN = Pattern.compile("jndi:/\\w+/\\w+(/.*)");
    
    private Map<String, SourceFactory> sourceFactories = Collections.emptyMap();
    
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public void setSourceFactories(Map<String, SourceFactory> sourceFactories) {
        this.sourceFactories = sourceFactories;
    }

    @Override
    public Source resolveURI(String location) throws MalformedURLException, IOException {
        String modifiedLocation = location;
        if (modifiedLocation.indexOf("context://") == 0) {
            modifiedLocation = location.substring("context://".length());
        } else {
            Matcher matcher = TOMCAT_URL_PATTERN.matcher(location);
            if (matcher.matches()) {
                modifiedLocation = matcher.group(1);
            }
        }
        URI uri;
        try {
            uri = new URI(modifiedLocation);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String scheme = uri.getScheme();
        SourceFactory factory = this.sourceFactories.get(scheme);
        if (factory != null) {
            return factory.getSource(location, null);
        }
        return new SpringResourceSource(this.resourceLoader.getResource(uri.toString()));
    }

    @Override
    public Source resolveURI(String location, String base, Map parameters) throws MalformedURLException, IOException {
        //TODO: really implement this . . .
        return resolveURI(location);
    }

    @Override
    public void release(Source source) {
    }
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        LanewebSourceResolver lsr = new LanewebSourceResolver();
        lsr.resolveURI("jndi:/localhost/laneweb/");
    }
}
