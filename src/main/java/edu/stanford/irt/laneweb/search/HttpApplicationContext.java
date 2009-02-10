package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.net.URL;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;

public class HttpApplicationContext extends AbstractXmlApplicationContext {

    private HttpResource[] configResources;

    
    public HttpApplicationContext(String url, String login, String password) throws IOException
    {
	URL urlObj = new URL(url);
	HttpResource resource = new HttpResource(urlObj, login, password);
	configResources = new HttpResource[1];
	configResources[0] = resource;
	this.refresh();
    }
    
    protected Resource[] getConfigResources() {
	return this.configResources;
    }
    
    
}


