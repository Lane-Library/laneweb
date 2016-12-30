package edu.stanford.irt.laneweb.config;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.XStream;

import edu.stanford.irt.cocoon.spring.SpringComponentFactory;
import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.cocoon.CacheFactoryBean;
import edu.stanford.irt.laneweb.eresources.search.Facet;

@Configuration
@ImportResource({
    "/WEB-INF/spring/applications.xmap",
    "/WEB-INF/spring/bookmarks.xmap",
    "/WEB-INF/spring/classes.xmap",
    "/WEB-INF/spring/content.xmap",
    "/WEB-INF/spring/eresources.xmap",
    "/WEB-INF/spring/mobile.xmap",
    "/WEB-INF/spring/rss.xmap",
    "/WEB-INF/spring/sitemap.xmap",
    "classpath:/net/bull/javamelody/monitoring-spring.xml"
})
@ComponentScan({
    "edu.stanford.irt.laneweb.config",
    "edu.stanford.irt.solr.service"
})
public class LanewebConfiguration {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer pspc = new PropertyPlaceholderConfigurer();
        pspc.setPlaceholderPrefix("%{");
        pspc.setIgnoreResourceNotFound(true);
        pspc.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        pspc.setLocations(new Resource[] { new ClassPathResource("/config/application.properties"),
                new FileSystemResource(System.getProperty("user.dir") + "/application.properties"),
                new FileSystemResource(System.getProperty("spring.config.location") + "/application.properties") });
        return pspc;
    }

    @Bean
    public CacheFactoryBean cache() throws URISyntaxException {
        return new CacheFactoryBean(jCacheManagerFactoryBean().getObject());
    }

    @Bean
    public SpringComponentFactory componentFactory() {
        return new SpringComponentFactory();
    }

    @Bean
    public JCacheManagerFactoryBean jCacheManagerFactoryBean() throws URISyntaxException {
        JCacheManagerFactoryBean factoryBean = new JCacheManagerFactoryBean();
        factoryBean.setCacheManagerUri(getClass().getResource("/ehcache.xml").toURI());
        return factoryBean;
    }

    @Bean
    public XStreamMarshaller marshaller() {
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
