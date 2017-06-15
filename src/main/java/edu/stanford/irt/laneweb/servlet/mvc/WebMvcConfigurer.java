package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ObjectMapper objectMapper;

    private RedirectProcessor redirectProcessor;

    private SolrImageService solrImageService;

    @Autowired
    public WebMvcConfigurer(final ObjectMapper objectMapper, final RedirectProcessor redirectProcessor,
            final SolrImageService solrImageService) {
        this.objectMapper = objectMapper;
        this.redirectProcessor = redirectProcessor;
        this.solrImageService = solrImageService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new PersistentLoginHandlerInterceptor())
            .addPathPatterns("/**/secure/**", "/**/redirect/cme/**");
        registry.addInterceptor(new DeviceResolverHandlerInterceptor())
            .addPathPatterns("/**/*.html");
        registry.addInterceptor(mobileSiteInterceptor())
             .addPathPatterns("/**/*.html");
        registry.addInterceptor(redirectHandlerInterceptor())
            .addPathPatterns("/**");
        registry.addInterceptor(new SearchImageInterceptor(this.solrImageService))
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
    public SimpleUrlHandlerMapping getSimpleUrlHandlerMapping(final ResourceHttpRequestHandler staticRequestHandler) {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(Collections.singletonMap("/**/*.*", staticRequestHandler));
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
    public RedirectHandlerInterceptor redirectHandlerInterceptor() {
        return new RedirectHandlerInterceptor(this.redirectProcessor);
    }

    @Bean
    public ResourceHttpRequestHandler staticRequestHandler(
            @Value("${edu.stanford.irt.laneweb.live-base}/") final URI liveBase) throws MalformedURLException {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Arrays.asList(new Resource[] { new ClassPathResource("/"),
                new ClassPathResource("/static/"), new UrlResource(liveBase.toURL()) }));
        handler.setCacheSeconds(ONE_YEAR_IN_SECONDS);
        handler.setSupportedMethods("HEAD", "GET");
        return handler;
    }
}
