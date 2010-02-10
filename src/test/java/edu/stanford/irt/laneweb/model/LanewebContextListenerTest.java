package edu.stanford.irt.laneweb.model;

import static org.easymock.classextension.EasyMock.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LanewebContextListenerTest {

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("java.naming.factory.initial", MockInitialContextFactory.class.getName());
    }

    private ServletContextEvent event;

    private ServletContextListener listener;

    private Context namingContext;

    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.listener = new LanewebContextListener();
        this.event = createMock(ServletContextEvent.class);
        this.servletContext = createMock(ServletContext.class);
        this.namingContext = createMock(Context.class);
        MockInitialContextFactory.setMockContext(this.namingContext);
    }

    @After
    public void tearDown() throws Exception {
        verifyMocks();
    }

    @Test
    public void testContextInitialized() throws NamingException {
        expect(this.event.getServletContext()).andReturn(this.servletContext);
        expect(this.servletContext.getInitParameter(isA(String.class))).andReturn(null).atLeastOnce();
        expect(this.namingContext.lookup(isA(String.class))).andReturn("file:/foo").atLeastOnce();
        this.servletContext.setAttribute(isA(String.class), isA(Object.class));
        expectLastCall().times(6);
        replayMocks();
        this.listener.contextInitialized(this.event);
    }

    private void replayMocks() {
        replay(this.namingContext);
        replay(this.event);
        replay(this.servletContext);
    }

    private void verifyMocks() {
        verify(this.namingContext);
        verify(this.event);
        verify(this.servletContext);
    }
}
