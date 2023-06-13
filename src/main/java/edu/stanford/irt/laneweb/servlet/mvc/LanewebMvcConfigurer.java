package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

@Configuration
@EnableWebMvc
public class LanewebMvcConfigurer implements WebMvcConfigurer {

    private static final Integer ONE_YEAR_IN_SECONDS = Integer.valueOf(31_536_000);

    private ObjectMapper objectMapper;

    private RedirectProcessor redirectProcessor;

    public LanewebMvcConfigurer(final ObjectMapper objectMapper, final RedirectProcessor redirectProcessor) {
        this.objectMapper = objectMapper;
        this.redirectProcessor = redirectProcessor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new PersistentLoginHandlerInterceptor()).addPathPatterns("/secure/**",
                "/redirect/cme/**");
        registry.addInterceptor(redirectHandlerInterceptor());
       
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
        handlerMapping.setUrlMap(Collections.singletonMap("/**", staticRequestHandler));
        handlerMapping.setDefaultHandler(new DefaultRequestHandler());
        handlerMapping.setInterceptors(redirectHandlerInterceptor());
        return handlerMapping;
    }

    @Bean
    public SitemapHandlerExceptionResolver getSitemapHandlerExceptionResolver(
            final SitemapController sitemapController) {
        return new SitemapHandlerExceptionResolver(sitemapController);
    }


    @Bean
    public RedirectHandlerInterceptor redirectHandlerInterceptor() {
        return new RedirectHandlerInterceptor(this.redirectProcessor);
    }

    @Bean
    public UrlBasedViewResolver redirectViewResolver() {
        UrlBasedViewResolver redirectViewResolver = new UrlBasedViewResolver();
        redirectViewResolver.setViewClass(RedirectView.class);
        return redirectViewResolver;
    }

    @Bean
    public ResourceHttpRequestHandler staticRequestHandler(
            @Value("${edu.stanford.irt.laneweb.live-base}/") final URI liveBase) throws MalformedURLException {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Arrays.asList(new ClassPathResource("/"), new ClassPathResource("/static/"),
                new UrlResource(liveBase.toURL())));
        handler.setCacheSeconds(ONE_YEAR_IN_SECONDS);
        handler.setSupportedMethods("HEAD", "GET");
        return handler;
    }
}
