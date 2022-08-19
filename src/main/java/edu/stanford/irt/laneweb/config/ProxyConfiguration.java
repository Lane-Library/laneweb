package edu.stanford.irt.laneweb.config;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetService;
import edu.stanford.irt.laneweb.proxy.ElementProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.HtmlProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.ProxyHostManager;
import edu.stanford.irt.laneweb.proxy.ProxyLinkSelector;
import edu.stanford.irt.laneweb.proxy.ProxyServersService;
import edu.stanford.irt.laneweb.proxy.SolrProxyServersService;

@Configuration
public class ProxyConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/search-proxy-links")
    @Scope("prototype")
    public Transformer elementProxyLinkTransformer() {
        return new ElementProxyLinkTransformer("url");
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/proxy-links")
    @Scope("prototype")
    public Transformer htmlProxyLinkTransformer(final ProxyHostManager proxyHostManager) {
        return new HtmlProxyLinkTransformer(proxyHostManager);
    }

    @Bean(destroyMethod = "destroy")
    public ProxyHostManager proxyHostManager(final ProxyServersService proxyServersService) {
        return new ProxyHostManager(proxyServersService, Executors.newScheduledThreadPool(1));
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/proxy-links")
    public Selector proxyLinkSelector() {
        return new ProxyLinkSelector();
    }

    @Bean
    public ProxyServersService proxyServersService(
            @Qualifier("edu.stanford.irt.laneweb.solr.FacetService") final FacetService facetService) {
        return new SolrProxyServersService(facetService);
    }
}
