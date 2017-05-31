package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;
import edu.stanford.irt.solr.service.SolrImageService;

@Configuration
@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static final Integer ONE_YEAR_IN_SECONDS = Integer.valueOf(31_536_000);

    private URL liveBase;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedirectProcessor redirectProcessor;

    @Autowired
    private SolrImageService solrImageService;

    public WebMvcConfigurer(@Value("${edu.stanford.irt.laneweb.live-base}/") final URL liveBase) {
        this.liveBase = liveBase;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(persistentLoginHandlerInterceptor())
            .addPathPatterns("/**/secure/**", "/**/redirect/cme/**");
        registry.addInterceptor(deviceResolverHandlerInterceptor())
            .addPathPatterns("/**/*.html");
        registry.addInterceptor(mobileSiteInterceptor())
             .addPathPatterns("/**/*.html");
        registry.addInterceptor(redirectHandlerInterceptor())
            .addPathPatterns("/**");
        registry.addInterceptor(searchImageInterceptor())
            .addPathPatterns("/search.html");
    }

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);
        converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
    }

    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public SimpleUrlHandlerMapping getSimpleUrlHandlerMapping(final ServletContext servletContext)
            throws MalformedURLException {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("/**/*.*", staticRequestHandler(servletContext));
        handlerMapping.setUrlMap(urlMap);
        handlerMapping.setDefaultHandler(new DefaultRequestHandler());
        handlerMapping.setInterceptors(new Object[] { redirectHandlerInterceptor() });
        return handlerMapping;
    }

    @Bean
    public SitemapHandlerExceptionResolver getSitemapHandlerExceptionResolver(
            final SitemapController sitemapController) {
        return new SitemapHandlerExceptionResolver(sitemapController);
    }

    @Bean
    public MobileSiteInterceptor mobileSiteInterceptor() {
        Map<String, String> redirectMap = new HashMap<>();
        redirectMap.put("/index.html", "/m/index.html");
        redirectMap.put("/biomed-resources/eb.html", "/m/book.html");
        redirectMap.put("/biomed-resources/ej.html", "/m/ej.html");
        return new MobileSiteInterceptor(redirectMap);
    }

    @Bean
    public PersistentLoginHandlerInterceptor persistentLoginHandlerInterceptor() {
        return new PersistentLoginHandlerInterceptor();
    }

    @Bean
    public RedirectHandlerInterceptor redirectHandlerInterceptor() {
        return new RedirectHandlerInterceptor(this.redirectProcessor);
    }

    @Bean
    public SearchImageInterceptor searchImageInterceptor() {
        return new SearchImageInterceptor(this.solrImageService);
    }

    @Bean
    public HttpRequestHandler staticRequestHandler(final ServletContext servletContext) throws MalformedURLException {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Arrays.asList(new Resource[] { new ClassPathResource("/"),
                new ServletContextResource(servletContext, "/"), new UrlResource(this.liveBase) }));
        handler.setCacheSeconds(ONE_YEAR_IN_SECONDS);
        handler.setSupportedMethods("HEAD", "GET");
        return handler;
    }
}
