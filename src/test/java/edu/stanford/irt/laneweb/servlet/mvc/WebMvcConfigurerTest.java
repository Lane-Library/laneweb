package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public class WebMvcConfigurerTest {

    private WebMvcConfigurer configuration;

    @Before
    public void setUp() {
        this.configuration = new WebMvcConfigurer("live-base");
    }

    @Test
    public void testAddInterceptors() {
        InterceptorRegistry registry = createMock(InterceptorRegistry.class);
        InterceptorRegistration registration = createMock(InterceptorRegistration.class);
        expect(registry.addInterceptor(isA(PersistentLoginHandlerInterceptor.class))).andReturn(registration);
        expect(registration.addPathPatterns("/**/secure/**", "/**/redirect/cme/**")).andReturn(registration);
        expect(registry.addInterceptor(isA(DeviceResolverHandlerInterceptor.class))).andReturn(registration);
        expect(registration.addPathPatterns("/**/*.html")).andReturn(registration);
        expect(registry.addInterceptor(isA(MobileSiteInterceptor.class))).andReturn(registration);
        expect(registration.addPathPatterns("/**/*.html")).andReturn(registration);
        expect(registry.addInterceptor(isA(RedirectHandlerInterceptor.class))).andReturn(registration);
        expect(registration.addPathPatterns("/**")).andReturn(registration);
        replay(registry, registration);
        this.configuration.addInterceptors(registry);
        verify(registry, registration);
    }

    @Test
    public void testAddResourceHandlers() {
        ResourceHandlerRegistry registry = createMock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration registration = createMock(ResourceHandlerRegistration.class);
        expect(registry.addResourceHandler("/**")).andReturn(registration);
        expect(registration.addResourceLocations("live-base/", "/")).andReturn(registration);
        expect(registration.setCachePeriod(Integer.valueOf(31536000))).andReturn(registration);
        replay(registry, registration);
        this.configuration.addResourceHandlers(registry);
        verify(registry, registration);
    }

    @Test
    public void testConfigureMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        this.configuration.configureMessageConverters(converters);
        assertSame(2, converters.size());
    }

    @Test
    public void testDeviceResolverHandlerInterceptor() {
        assertNotNull(this.configuration.deviceResolverHandlerInterceptor());
    }

    @Test
    public void testGetSitemapHandlerExceptionResolver() {
        assertNotNull(this.configuration.getSitemapHandlerExceptionResolver(null));
    }

    @Test
    public void testMobileSiteInterceptor() {
        assertNotNull(this.configuration.mobileSiteInterceptor());
    }

    @Test
    public void testPersistentLoginHandlerInterceptor() {
        assertNotNull(this.configuration.persistentLoginHandlerInterceptor());
    }

    @Test
    public void testRedirectHandlerInterceptor() {
        assertNotNull(this.configuration.redirectHandlerInterceptor());
    }
}
