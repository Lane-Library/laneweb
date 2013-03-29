package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.net.URL;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;

public class HttpApplicationContext extends AbstractXmlApplicationContext {

    private HttpResource[] configResources;

    public HttpApplicationContext(final String url, final String login, final String password) throws IOException {
        URL urlObj = new URL(url);
        HttpResource resource = new HttpResource(urlObj, login, password);
        this.configResources = new HttpResource[1];
        this.configResources[0] = resource;
        this.refresh();
    }

    @Override
    protected Resource[] getConfigResources() {
        return this.configResources;
    }
}
