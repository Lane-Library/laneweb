package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;


public class LanewebSourceResolver implements SourceResolver, ResourceLoaderAware {

    // This part is necessary because EnvironmentHelper constructor uses a jndi:/localhost/ url string
    private Pattern tomcatURLPattern;
    
    private Map<String, SourceFactory> sourceFactories = Collections.emptyMap();
    
    private ResourceLoader resourceLoader;
    
    private SitemapSourceLocationModifier locationModifier = new SitemapSourceLocationModifier();

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public void setSourceFactories(Map<String, SourceFactory> sourceFactories) {
        this.sourceFactories = sourceFactories;
    }
    
    public void setServletContext(ServletContext servletContext) {
        this.tomcatURLPattern = Pattern.compile("jndi:/localhost" + servletContext.getContextPath() + "(/.*)");
    }

    public Source resolveURI(String location) throws MalformedURLException, IOException {
        String modifiedLocation = location;
        Matcher matcher = this.tomcatURLPattern.matcher(location);
        if (matcher.matches()) {
            modifiedLocation = matcher.group(1);
        }
        int colonPosition = modifiedLocation.indexOf(':');
        if (colonPosition > 0) {
            modifiedLocation = this.locationModifier.modify(modifiedLocation);
            colonPosition = modifiedLocation.indexOf(':');
            String scheme = modifiedLocation.substring(0, colonPosition);
            SourceFactory factory = this.sourceFactories.get(scheme);
            if (factory != null) {
                return factory.getSource(modifiedLocation, null);
            }
        }
        return new SpringResourceSource(this.resourceLoader.getResource(modifiedLocation));
    }

    @SuppressWarnings("rawtypes")
    public Source resolveURI(String location, String base, Map parameters) throws MalformedURLException, IOException {
        //TODO: really implement this . . .
        return resolveURI(location);
    }

    public void release(Source source) {
    }
}
