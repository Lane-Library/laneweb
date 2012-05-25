package edu.stanford.irt.cocoon.source;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

public class SourceResolverImpl implements SourceResolver {

    private SourceFactory defaultFactory;

    private LocationModifier locationModifier = new LocationModifier();

    private Map<String, SourceFactory> sourceFactories = Collections.emptyMap();

    public void release(final Source source) {
    }

    public Source resolveURI(final String location) throws IOException {
        String modifiedLocation = location;
        // Matcher matcher = this.tomcatURLPattern.matcher(location);
        // if (matcher.matches()) {
        // modifiedLocation = matcher.group(1);
        // }
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
        return this.defaultFactory.getSource(modifiedLocation, null);
    }

    public Source resolveURI(final String location, final String base, final Map parameters) throws IOException {
        return resolveURI(location);
    }

    public void setDefaultFactory(final SourceFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public void setSourceFactories(final Map<String, SourceFactory> sourceFactories) {
        this.sourceFactories = sourceFactories;
    }
}
