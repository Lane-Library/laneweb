package edu.stanford.irt.laneweb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.proxy.ElementProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.EzproxyServersWriter;
import edu.stanford.irt.laneweb.proxy.HtmlProxyLinkTransformer;
import edu.stanford.irt.laneweb.proxy.ProxyHostManager;
import edu.stanford.irt.laneweb.proxy.ProxyLinkSelector;

@Configuration
public class ProxyConfiguration {

    private DataSource dataSource;

    @Autowired
    public ProxyConfiguration(@Qualifier("javax.sql.DataSource/voyager") final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/search-proxy-links")
    @Scope("prototype")
    public Transformer elementProxyLinkTransformer() {
        return new ElementProxyLinkTransformer("url");
    }

    @Bean
    public EzproxyServersWriter ezproxyServersWriter() throws IOException {
        return new EzproxyServersWriter(this.dataSource, proxySQLProperties());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/proxy-links")
    @Scope("prototype")
    public Transformer htmlProxyLinkTransformer() {
        return new HtmlProxyLinkTransformer(proxyHostManager());
    }

    public ProxyHostManager proxyHostManager() {
        return new ProxyHostManager(this.dataSource);
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/proxy-links")
    public Selector proxyLinkSelector() {
        return new ProxyLinkSelector();
    }

    @Bean
    public Properties proxySQLProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass()
                .getResourceAsStream("/edu/stanford/irt/laneweb/proxy/ezproxy-servers-sql.properties")) {
            properties.load(input);
        }
        return properties;
    }
}
