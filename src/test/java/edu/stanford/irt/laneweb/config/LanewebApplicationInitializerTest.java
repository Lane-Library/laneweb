package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

public class LanewebApplicationInitializerTest {

    private Dynamic dynamic;

    private FilterRegistration filterRegistration;

    private LanewebApplicationInitializer initializer;

    private ServletContext servletContext;

    @Before
    public void setUp() {
        this.initializer = new LanewebApplicationInitializer();
        this.servletContext = mock(ServletContext.class);
        this.filterRegistration = mock(FilterRegistration.class);
        this.dynamic = mock(Dynamic.class);
    }

    @Test
    public void testOnStartup() throws ServletException {
        this.servletContext.addListener(isA(ContextLoaderListener.class));
        expect(this.servletContext.addServlet(eq("dispatcher"), isA(DispatcherServlet.class))).andReturn(this.dynamic);
        this.dynamic.setLoadOnStartup(1);
        expect(this.dynamic.addMapping("/")).andReturn(Collections.emptySet());
        this.dynamic.setAsyncSupported(true);
        expect(this.servletContext.getFilterRegistration("javamelody")).andReturn(this.filterRegistration);
        expect(this.filterRegistration.setInitParameter("storage-directory", "null/logs/javamelody")).andReturn(true);
        replay(this.servletContext, this.filterRegistration, this.dynamic);
        this.initializer.onStartup(this.servletContext);
        verify(this.servletContext, this.filterRegistration, this.dynamic);
    }
}
