package edu.stanford.irt.laneweb.servlet.mvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.DelegatingThemeSource;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.servlet.theme.FixedThemeResolver;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

import edu.stanford.irt.laneweb.mapping.LanewebObjectMapper;
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

    private static final int ONE_YEAR_IN_SECONDS = 31536000;

    @Autowired
    private DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor;

    @Autowired
    private MobileSiteInterceptor mobileSiteInterceptor;

    @Autowired
    private LanewebObjectMapper objectMapper;

    @Autowired
    private PersistentLoginHandlerInterceptor persistentLoginHandlerInterceptor;

    @Autowired
    private RedirectHandlerInterceptor redirectHandlerInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.persistentLoginHandlerInterceptor)
            .addPathPatterns("/**/secure/**", "/**/redirect/cme/**");
        registry.addInterceptor(this.deviceResolverHandlerInterceptor)
            .addPathPatterns("/**/*.html");
        registry.addInterceptor(this.mobileSiteInterceptor)
             .addPathPatterns("/**/*.html");
        registry.addInterceptor(this.redirectHandlerInterceptor)
            .addPathPatterns("/**");
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

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster getApplicationEventMulticaster() {
        return new SimpleApplicationEventMulticaster();
    }

    @Bean(name = "dispatcherServlet")
    public DispatcherServlet getDispatcherServlet() {
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setDetectAllHandlerMappings(true);
        servlet.setDetectAllHandlerAdapters(true);
        servlet.setDetectAllHandlerExceptionResolvers(false);
        servlet.setDetectAllViewResolvers(false);
        servlet.setPublishContext(false);
        servlet.setPublishEvents(false);
        return servlet;
    }

    @Bean(name = "flashMapManager")
    public FlashMapManager getFlashMapManager() {
        return new SessionFlashMapManager();
    }

    @Bean(name = "handlerAdapter")
    public HandlerAdapter getHandlerAdapter() {
        return new HttpRequestHandlerAdapter();
    }

    @Bean(name = "lifecycleProcessor")
    public LifecycleProcessor getLifecycleProcessor() {
        return new DefaultLifecycleProcessor();
    }

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageSource() {
        return new DelegatingMessageSource();
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver getMultipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean(name = "viewNameTranslator")
    public RequestToViewNameTranslator getRequestToViewNameTranslator() {
        return new DefaultRequestToViewNameTranslator();
    }

    @Bean
    public SimpleUrlHandlerMapping getSimpleUrlHandlerMapping(
            @Qualifier(value = "org.springframework.web.servlet.resource.ResourceHttpRequestHandler/static") final HttpRequestHandler staticHandler) {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("/**/*.*", staticHandler);
        handlerMapping.setUrlMap(urlMap);
        handlerMapping.setDefaultHandler(new DefaultRequestHandler());
        handlerMapping.setInterceptors(new Object[] { this.redirectHandlerInterceptor });
        return handlerMapping;
    }

    @Bean
    public SitemapHandlerExceptionResolver getSitemapHandlerExceptionResolver(
            final SitemapController sitemapController) {
        return new SitemapHandlerExceptionResolver(sitemapController);
    }

    @Bean(name = "org.springframework.web.servlet.resource.ResourceHttpRequestHandler/static")
    public HttpRequestHandler getStaticRequestHandler(final ServletContext servletContext,
            @Value(value = "${edu.stanford.irt.laneweb.live-base}/") final UrlResource liveBase) {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(
                Arrays.asList(new Resource[] { new ServletContextResource(servletContext, "/"), liveBase }));
        handler.setCacheSeconds(ONE_YEAR_IN_SECONDS);
        handler.setSupportedMethods("HEAD", "GET");
        return handler;
    }

    @Bean(name = "themeResolver")
    public ThemeResolver getThemeResolver() {
        return new FixedThemeResolver();
    }

    @Bean(name = "themeSource")
    public ThemeSource getThemeSource() {
        return new DelegatingThemeSource();
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
    public RedirectHandlerInterceptor redirectHandlerInterceptor(final RedirectProcessor redirectProcessor) {
        return new RedirectHandlerInterceptor(redirectProcessor);
    }
}
