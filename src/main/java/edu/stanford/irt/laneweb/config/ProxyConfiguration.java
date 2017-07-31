package edu.stanford.irt.laneweb.config;

import static edu.stanford.irt.laneweb.util.IOUtils.getResourceAsString;

import java.net.URI;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.proxy.ElementProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.HTTPProxyServersService;
import edu.stanford.irt.laneweb.proxy.HtmlProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.JDBCProxyServersService;
import edu.stanford.irt.laneweb.proxy.ProxyHostManager;
import edu.stanford.irt.laneweb.proxy.ProxyLinkSelector;
import edu.stanford.irt.laneweb.proxy.ProxyServersService;

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

    @Bean
    @Profile("gce")
    public ProxyServersService httpProxyServersService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI) {
        return new HTTPProxyServersService(objectMapper, catalogServiceURI);
    }

    @Bean
    @Profile("!gce")
    public ProxyServersService jdbcProxyServersService(
            @Qualifier("javax.sql.DataSource/catalog") final DataSource dataSource) {
        return new JDBCProxyServersService(dataSource,
                getResourceAsString(ProxyServersService.class, "getProxyHosts.sql"),
                getResourceAsString(ProxyServersService.class, "ezproxyServers.sql"));
    }

    @Bean(destroyMethod = "destroy")
    public ProxyHostManager proxyHostManager(final ProxyServersService proxyServersService) {
        return new ProxyHostManager(proxyServersService, Executors.newScheduledThreadPool(1));
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/proxy-links")
    public Selector proxyLinkSelector() {
        return new ProxyLinkSelector();
    }
}
