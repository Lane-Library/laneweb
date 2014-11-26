package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.net.URL;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class HttpApplicationContext extends AbstractXmlApplicationContext {

    private Resource[] configResources;

    public HttpApplicationContext(final String url) throws IOException {
        URL urlObj = new URL(url);
        Resource resource = new UrlResource(urlObj);
        this.configResources = new Resource[1];
        this.configResources[0] = resource;
        this.refresh();
    }

    @Override
    protected Resource[] getConfigResources() {
        return this.configResources;
    }
}
