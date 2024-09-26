package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.XStream;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.spring.SpringComponentFactory;
import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.cocoon.CacheFactoryBean;
import edu.stanford.irt.laneweb.model.Model;
import jakarta.servlet.ServletContext;

@Configuration
@ImportResource({ "classpath:/spring/applications.xmap", "classpath:/spring/bookmarks.xmap",
        "classpath:/spring/libcals.xmap", "classpath:/spring/content.xmap", "classpath:/spring/eresources.xmap",
        "classpath:/spring/libguides.xmap", "classpath:/spring/rss.xmap", "classpath:/spring/sitemap.xmap" })
public class LanewebConfiguration {

    private Map<String, Object> constants;

    public LanewebConfiguration(@Qualifier("java.net.URI/libguide-service") final URI libguideServiceURI,
            @Qualifier("java.net.URI/libcal-service") final URI libcalServiceURI,
            @Value("${edu.stanford.irt.laneweb.live-base}") final URI contentBase,
            @Value("${edu.stanford.irt.laneweb.bookmarking}") final String bookmarking, ServletContext servletContext,
            @Value("${edu.stanford.irt.laneweb.version}") final String version,
            @Value("${edu.stanford.irt.laneweb.browzine-token}") final String browzineToken) {
        this.constants = new HashMap<>();
        this.constants.put(Model.BASE_PATH, servletContext.getContextPath());
        this.constants.put(Model.LIBGUIDE_SERVICE_URI, libguideServiceURI);
        this.constants.put(Model.LIBCAL_SERVICE_URI, libcalServiceURI);
        this.constants.put(Model.CONTENT_BASE, contentBase);
        this.constants.put(Model.BOOKMARKING, bookmarking);
        this.constants.put(Model.VERSION, version);
        this.constants.put(Model.BROWZINE_TOKEN, browzineToken);
        // set the http.agent system property:
        System.setProperty("http.agent", "laneweb-" + version);
    }

    @Bean
    CacheFactoryBean cache() throws URISyntaxException {
        return new CacheFactoryBean(jCacheManagerFactoryBean().getObject());
    }

    @Bean
    ComponentFactory componentFactory(BeanFactory beanFactory) {
        return new SpringComponentFactory(beanFactory);
    }

    @Bean
    JCacheManagerFactoryBean jCacheManagerFactoryBean() throws URISyntaxException {
        JCacheManagerFactoryBean factoryBean = new JCacheManagerFactoryBean();
        factoryBean.setCacheManagerUri(getClass().getResource("/ehcache.xml").toURI());
        return factoryBean;
    }

    @Bean
    Marshaller marshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("bookmark", Bookmark.class);
        marshaller.setAliases(aliases);
        marshaller.setMode(XStream.NO_REFERENCES);
        return marshaller;
    }

    @Bean(name = "edu.stanford.irt.cocoon.Model")
    @Scope("request")
    Map<String, Object> model() {
        return new HashMap<>(this.constants);
    }

}
