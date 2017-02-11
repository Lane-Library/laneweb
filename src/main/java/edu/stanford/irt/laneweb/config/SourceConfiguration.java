package edu.stanford.irt.laneweb.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
import edu.stanford.irt.laneweb.cocoon.CacheSourceResolver;
import edu.stanford.irt.laneweb.cocoon.CachedXMLSourceResolver;

@Configuration
public class SourceConfiguration implements InitializingBean {

    private static final List<String> SITEMAPS = Arrays
            .asList(new String[] { "content", "applications", "eresources", "rss", "mobile", "classes", "bookmarks" });

    private BeanFactory beanFactory;

    @Autowired
    private Cache<Serializable, CachedResponse> cache;

    @Autowired
    private ComponentFactory componentFactory;

    @Autowired
    private ResourceLoader resourceLoader;

    private SourceResolverImpl sourceResolver;

    @Autowired
    public SourceConfiguration(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean(name = "edu.stanford.irt.cocoon.source.SourceResolver")
    public SourceResolver sourceResolver() {
        return this.sourceResolver;
    }

    @Override
    public void afterPropertiesSet() {
        this.sourceResolver = new SourceResolverImpl();
        SitemapSourceResolver sitemapSourceResolver = new SitemapSourceResolver(this.componentFactory, sourceResolver) {

            @SuppressWarnings("unchecked")
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
        SourceResolver cacheSourceResolver = new CacheSourceResolver(this.cache, sourceResolver);
        Map<String, SourceResolver> sourceResolvers = new HashMap<>();
        sourceResolvers.put("cocoon", sitemapSourceResolver);
        sourceResolvers.put("cache", cacheSourceResolver);
        sourceResolvers.put("cachedxml", new CachedXMLSourceResolver(this.beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/xml", SAXParser.class), this.cache, this.sourceResolver));
        sourceResolver.setSourceResolvers(sourceResolvers);
        SpringResourceSourceResolver springResourceSourceResolver = new SpringResourceSourceResolver();
        springResourceSourceResolver.setResourceLoader(this.resourceLoader);
        sourceResolver.setDefaultResolver(springResourceSourceResolver);
    }
}
