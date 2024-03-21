package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thoughtworks.xstream.XStream;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.spring.SpringComponentFactory;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.cocoon.CacheFactoryBean;
import edu.stanford.irt.laneweb.mapping.ApplicationStatusDeserializer;
import edu.stanford.irt.laneweb.mapping.CourseDeserializer;
import edu.stanford.irt.laneweb.mapping.CourseReservesItemListDeserializer;
import edu.stanford.irt.laneweb.mapping.IPGroupSerializer;
import edu.stanford.irt.laneweb.mapping.ResultDeserializer;
import edu.stanford.irt.laneweb.mapping.TicketSerializer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.status.ApplicationStatus;
import jakarta.servlet.ServletContext;

@Configuration
@ImportResource({ "classpath:/spring/applications.xmap", "classpath:/spring/bookmarks.xmap",
        "classpath:/spring/libcals.xmap", "classpath:/spring/content.xmap", "classpath:/spring/eresources.xmap",
        "classpath:/spring/libguides.xmap", "classpath:/spring/rss.xmap", "classpath:/spring/sitemap.xmap" })
@ComponentScan({ "edu.stanford.irt.laneweb.config", "edu.stanford.irt.solr.service",
        "edu.stanford.irt.laneweb.servlet.mvc", "edu.stanford.irt.laneweb.bookmarks",
        "edu.stanford.irt.laneweb.livechat" })
public class LanewebConfiguration {

    private static final List<String> DEFAULT_LOCATIONS = Arrays
            .asList("classpath:/,classpath:/config/,file:./,file:./config/".split(","));

    private static final int HTTP_CONNECT_TIMEOUT = 5;

    private static final int HTTP_READ_TIMEOUT = 20;

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
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(
            final Environment environment, final ResourceLoader resourceLoader) {
        List<Resource> locations = DEFAULT_LOCATIONS.stream().map((final String s) -> s + "application.properties")
                .map(resourceLoader::getResource).collect(Collectors.toList());
        Arrays.stream(environment.getActiveProfiles())
                .forEach((final String profile) -> locations.addAll(
                        DEFAULT_LOCATIONS.stream().map((final String s) -> s + "application-" + profile + ".properties")
                                .map(resourceLoader::getResource).collect(Collectors.toList())));
        String springConfigProperty = environment.getProperty("spring.config.location");
        if (springConfigProperty != null) {
            locations.addAll(Arrays.stream(springConfigProperty.split(",")).map(resourceLoader::getResource)
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
    public ComponentFactory componentFactory(BeanFactory beanFactory) {
        return new SpringComponentFactory(beanFactory);
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
        marshaller.setAliases(aliases);
        marshaller.setMode(XStream.NO_REFERENCES);
        return marshaller;
    }

    @Bean(name = "edu.stanford.irt.cocoon.Model")
    @Scope("request")
    public Map<String, Object> model() {
        return new HashMap<>(this.constants);
    }

    @Bean
    HttpComponentsClientHttpRequestFactory requestFactory() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .setResponseTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    List<HttpMessageConverter<?>> getMessageConverters(ObjectMapper objectMapper) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        messageConverters.add(stringConverter);
        messageConverters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        messageConverters.add(new ResourceHttpMessageConverter());
        return messageConverters;
    }

    @Bean
    RestClient getRestClient(final List<HttpMessageConverter<?>> messageConverters,
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
        return RestClient.builder().requestFactory(httpComponentsClientHttpRequestFactory)
                .messageConverters(converters -> converters.addAll(0, messageConverters)).build();
    }

    @Bean
    RESTService restService(final RestClient restClient) {
        return new RESTService(restClient);
    }
}
