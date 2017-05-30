package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.proxy.ElementProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.HTTPProxyServersService;
import edu.stanford.irt.laneweb.proxy.HtmlProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.ProxyHostManager;
import edu.stanford.irt.laneweb.proxy.ProxyLinkSelector;
import edu.stanford.irt.laneweb.proxy.ProxyServersService;

@Configuration
public class ProxyConfiguration {

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    @Autowired
    public ProxyConfiguration(final ObjectMapper objectMapper, final URI catalogServiceURI) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/search-proxy-links")
    @Scope("prototype")
    public Transformer elementProxyLinkTransformer() {
        return new ElementProxyLinkTransformer("url");
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/proxy-links")
    @Scope("prototype")
    public Transformer htmlProxyLinkTransformer() {
        return new HtmlProxyLinkTransformer(proxyHostManager());
    }

    @Bean(destroyMethod = "destroy")
    public ProxyHostManager proxyHostManager() {
        return new ProxyHostManager(proxyServersService(), Executors.newScheduledThreadPool(1));
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/proxy-links")
    public Selector proxyLinkSelector() {
        return new ProxyLinkSelector();
    }

    @Bean
    public ProxyServersService proxyServersService() {
        return new HTTPProxyServersService(this.objectMapper, this.catalogServiceURI);
    }
}
