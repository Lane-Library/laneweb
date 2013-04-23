package edu.stanford.irt.laneweb.cocoon;

import java.util.Collections;
import java.util.Map;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;

public class SpringResourceSourceResolver implements SourceResolver, ResourceLoaderAware {

    private LocationModifier locationModifier = new LocationModifier();

    private ResourceLoader resourceLoader;

    private Map<String, SourceResolver> sourceResolvers = Collections.emptyMap();

    public Source resolveURI(final String location) {
        String modifiedLocation = location;
        int colonPosition = modifiedLocation.indexOf(':');
        if (colonPosition > 0) {
            modifiedLocation = this.locationModifier.modify(modifiedLocation);
            colonPosition = modifiedLocation.indexOf(':');
            String scheme = modifiedLocation.substring(0, colonPosition);
            SourceResolver factory = this.sourceResolvers.get(scheme);
            if (factory != null) {
                return factory.resolveURI(modifiedLocation);
            }
        }
        return new SpringResourceSource(this.resourceLoader.getResource(location));
    }

    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public void setSourceResolvers(final Map<String, SourceResolver> sourceResolvers) {
        this.sourceResolvers = sourceResolvers;
    }
}
