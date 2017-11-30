package edu.stanford.irt.laneweb.config;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.XStream;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.spring.SpringComponentFactory;
import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.cocoon.CacheFactoryBean;
import edu.stanford.irt.laneweb.eresources.search.Facet;

@Configuration
@ImportResource({
    "classpath:/spring/applications.xmap",
    "classpath:/spring/bookmarks.xmap",
    "classpath:/spring/classes.xmap",
    "classpath:/spring/content.xmap",
    "classpath:/spring/eresources.xmap",
    "classpath:/spring/mobile.xmap",
    "classpath:/spring/rss.xmap",
    "classpath:/spring/sitemap.xmap",
    "classpath:/net/bull/javamelody/monitoring-spring.xml"
})
@ComponentScan({
    "edu.stanford.irt.laneweb.config",
    "edu.stanford.irt.solr.service",
    "edu.stanford.irt.laneweb.servlet.mvc",
    "edu.stanford.irt.laneweb.bookmarks"
})
public class LanewebConfiguration {
    
    private static final List<String> DEFAULT_LOCATIONS =
            Arrays.asList("classpath:/,classpath:/config/,file:./,file:./config/".split(","));

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(
            final Environment environment, final ResourceLoader resourceLoader) {
        List<Resource> locations = DEFAULT_LOCATIONS.stream()
                .map((final String s) -> s + "application.properties")
                .map(resourceLoader::getResource)
                .collect(Collectors.toList());
        String springConfigProperty = environment.getProperty("spring.config.location");
        if (springConfigProperty != null) {
            locations.addAll(Arrays.stream(springConfigProperty.split(","))
                    .map(resourceLoader::getResource)
                    .collect(Collectors.toList()));
        }
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setIgnoreResourceNotFound(true);
        pspc.setLocations(locations.toArray(new Resource[locations.size()]));
        return pspc;
    }

    @Bean
    public CacheFactoryBean cache() throws URISyntaxException {
        return new CacheFactoryBean(jCacheManagerFactoryBean().getObject());
    }

    @Bean
    public ComponentFactory componentFactory() {
        return new SpringComponentFactory();
    }

    @Bean
    public JCacheManagerFactoryBean jCacheManagerFactoryBean() throws URISyntaxException {
        JCacheManagerFactoryBean factoryBean = new JCacheManagerFactoryBean();
        factoryBean.setCacheManagerUri(getClass().getResource("/ehcache.xml").toURI());
        return factoryBean;
    }

    @Bean
    public Marshaller marshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("bookmark", Bookmark.class);
        aliases.put("facet", Facet.class);
        marshaller.setAliases(aliases);
        marshaller.setMode(XStream.NO_REFERENCES);
        return marshaller;
    }

    @Bean(name = "edu.stanford.irt.cocoon.Model")
    @Scope("request")
    public Map<String, Object> model() {
        return new HashMap<>();
    }
}
