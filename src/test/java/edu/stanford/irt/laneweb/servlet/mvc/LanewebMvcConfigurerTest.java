package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class LanewebMvcConfigurerTest {

    private LanewebMvcConfigurer configuration;

    @Before
    public void setUp() {
        this.configuration = new LanewebMvcConfigurer(null, null);
    }

    @Test
    public void testAddInterceptors() {
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        InterceptorRegistration registration = mock(InterceptorRegistration.class);
        expect(registry.addInterceptor(isA(PersistentLoginHandlerInterceptor.class))).andReturn(registration);
        expect(registration.addPathPatterns("/secure/**", "/redirect/cme/**")).andReturn(registration);
        expect(registry.addInterceptor(isA(RedirectHandlerInterceptor.class))).andReturn(registration);
        replay(registry, registration);
        this.configuration.addInterceptors(registry);
        verify(registry, registration);
    }

    @Test
    public void testConfigureMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        this.configuration.configureMessageConverters(converters);
        assertSame(2, converters.size());
    }

    @Test
    public void testGetSitemapHandlerExceptionResolver() {
        assertNotNull(this.configuration.getSitemapHandlerExceptionResolver(null));
    }


    @Test
    public void testRedirectHandlerInterceptor() {
        assertNotNull(this.configuration.redirectHandlerInterceptor());
    }
}
