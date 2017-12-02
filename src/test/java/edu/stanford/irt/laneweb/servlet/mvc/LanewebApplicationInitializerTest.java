package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionTrackingMode;

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
    public void testOnStartup() {
        expect(this.servletContext.getFilterRegistration("javamelody")).andReturn(this.filterRegistration);
        expect(this.filterRegistration.setInitParameter("storage-directory", "null/logs/javamelody")).andReturn(true);
        this.servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        expect(this.servletContext.setInitParameter("webAppRootKey", "laneweb")).andReturn(true);
        this.servletContext.addListener(isA(ContextLoaderListener.class));
        expect(this.servletContext.addServlet(eq("DispatcherServlet"), isA(DispatcherServlet.class)))
                .andReturn(this.dynamic);
        this.dynamic.setLoadOnStartup(1);
        expect(this.dynamic.addMapping("/")).andReturn(Collections.emptySet());
        replay(this.servletContext, this.filterRegistration, this.dynamic);
        this.initializer.onStartup(this.servletContext);
        verify(this.servletContext, this.filterRegistration, this.dynamic);
    }
}