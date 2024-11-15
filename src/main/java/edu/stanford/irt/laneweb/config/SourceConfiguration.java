package edu.stanford.irt.laneweb.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.source.SitemapSourceResolver;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.source.SourceResolverImpl;
import edu.stanford.irt.cocoon.spring.SpringResourceSourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLByteStreamInterpreter;
import edu.stanford.irt.laneweb.cocoon.CacheSourceResolver;
import edu.stanford.irt.laneweb.cocoon.CachedXMLSourceResolver;

@Configuration
public class SourceConfiguration implements InitializingBean {

    private static final List<String> SITEMAPS = Arrays.asList("content", "applications", "eresources",
            "libcals", "bookmarks", "libguides");

    private BeanFactory beanFactory;

    private Cache<Serializable, CachedResponse> cache;

    private ComponentFactory componentFactory;

    private ResourceLoader resourceLoader;

    private SourceResolverImpl sourceResolver;

    private XMLByteStreamInterpreter xmlByteStreamInterpreter;

    public SourceConfiguration(final BeanFactory beanFactory, final Cache<Serializable, CachedResponse> cache,
            final ComponentFactory componentFactory, final ResourceLoader resourceLoader,
            final XMLByteStreamInterpreter xmlByteStreamInterpreter) {
        this.beanFactory = beanFactory;
        this.cache = cache;
        this.componentFactory = componentFactory;
        this.resourceLoader = resourceLoader;
        this.xmlByteStreamInterpreter = xmlByteStreamInterpreter;
    }

    @Override
    public void afterPropertiesSet() {
        this.sourceResolver = new SourceResolverImpl();
        SitemapSourceResolver sitemapSourceResolver = new SitemapSourceResolver(this.componentFactory,
                this.sourceResolver) {

            @Override
            protected Map<String, Object> getModel() {
                return SourceConfiguration.this.beanFactory.getBean("edu.stanford.irt.cocoon.Model", Map.class);
            }
        };
        Map<String, Sitemap> sitemaps = new HashMap<>();
        for (String name : SITEMAPS) {
            Sitemap sitemap = this.beanFactory.getBean(Sitemap.class.getName() + "/" + name, Sitemap.class);
            if ("applications".equals(name)) {
                sitemaps.put("apps", sitemap);
            } else {
                sitemaps.put(name, sitemap);
            }
        }
        sitemapSourceResolver.setSitemaps(sitemaps);
        SourceResolver cacheSourceResolver = new CacheSourceResolver(this.cache, this.sourceResolver);
        Map<String, SourceResolver> sourceResolvers = new HashMap<>();
        sourceResolvers.put("cocoon", sitemapSourceResolver);
        sourceResolvers.put("cache", cacheSourceResolver);
        sourceResolvers.put("cachedxml",
                new CachedXMLSourceResolver(
                        this.beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/xml", SAXParser.class),
                        this.cache, this.sourceResolver, this.xmlByteStreamInterpreter));
        this.sourceResolver.setSourceResolvers(sourceResolvers);
        this.sourceResolver.setDefaultResolver(new SpringResourceSourceResolver(this.resourceLoader));
    }

    @Bean(name = "edu.stanford.irt.cocoon.source.SourceResolver")
    public SourceResolver sourceResolver() {
        return this.sourceResolver;
    }
}
