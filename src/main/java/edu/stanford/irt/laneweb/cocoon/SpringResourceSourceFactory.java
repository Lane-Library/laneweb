package edu.stanford.irt.laneweb.cocoon;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

public class SpringResourceSourceFactory implements SourceFactory, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public Source getSource(final String location) {
        return new SpringResourceSource(this.resourceLoader.getResource(location));
    }

    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
