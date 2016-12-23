package edu.stanford.irt.laneweb.servlet.mvc;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "edu.stanford.irt.laneweb.servlet.mvc",
        "edu.stanford.irt.laneweb.bookmarks"})
@PropertySource(value={
    "classpath:/config/application.properties",
    "file:${user.dir}/application.properties",
    "file:${spring.config.location}/application.properties"},
     ignoreResourceNotFound=true)
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static final Integer ONE_YEAR_IN_SECONDS = Integer.valueOf(31_536_000);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedirectProcessor redirectProcessor;

    private String liveBase;

    public WebMvcConfigurer(@Value("${edu.stanford.irt.laneweb.live-base}") final String liveBase) {
        this.liveBase = liveBase + "/";
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
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations(new String[] {this.liveBase, "/" })
            .setCachePeriod(ONE_YEAR_IN_SECONDS);
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
}
