package edu.stanford.irt.laneweb.cocoon;

import java.net.URI;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;

public class SpringResourceSourceResolver implements SourceResolver, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public Source resolveURI(final URI location) {
        return new SpringResourceSource(this.resourceLoader.getResource(location.toString()));
    }

    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
