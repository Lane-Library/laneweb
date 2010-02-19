package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoaderListener;

// $Id$
public class LanewebIntegrationBase {

    private static class DS implements DataSource {

        public Connection getConnection() throws SQLException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }

        public Connection getConnection(final String username, final String password) throws SQLException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }

        public int getLoginTimeout() throws SQLException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }

        public PrintWriter getLogWriter() throws SQLException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }

        public void setLoginTimeout(final int seconds) throws SQLException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }

        public void setLogWriter(final PrintWriter out) throws SQLException {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @param args
     * @throws IOException
     * @throws ServletException
     * @throws NamingException
     * @throws IllegalStateException
     */
    //@Test
    public void run() throws ServletException, IOException, IllegalStateException, NamingException {
        final SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        DataSource ds = new DS();
        builder.bind("java:comp/env/jdbc/voyager", ds);
        builder.bind("java:comp/env/jdbc/eresources", ds);
        builder.bind("java:comp/env/ezproxy-key", "foo");
        builder.bind("java:comp/env/webdash-key", "foo");
        builder.activate();
        MockServletContext servletContext = new MockServletContext("src/main/webapp", new FileSystemResourceLoader());
        servletContext.addInitParameter("contextConfigLocation", "/WEB-INF/cocoon/spring/bassett.xml "
                + "/WEB-INF/cocoon/spring/cocoon.xml " + "/WEB-INF/cocoon/spring/datasources.xml "
                + "/WEB-INF/cocoon/spring/eresources.xml " + "/WEB-INF/cocoon/spring/history.xml "
                + "/WEB-INF/cocoon/spring/idc9.xml " + "/WEB-INF/cocoon/spring/laneweb.xml "
                + "/WEB-INF/cocoon/spring/merged-search.xml " + "/WEB-INF/cocoon/spring/model.xml "
                + "/WEB-INF/cocoon/spring/querymap.xml " + "/WEB-INF/cocoon/spring/search.xml "
                + "/WEB-INF/cocoon/spring/suggest.xml");
        ServletContextListener contextListener = new ContextLoaderListener();
        ServletContextEvent event = new ServletContextEvent(servletContext);
        contextListener.contextInitialized(event);
    }
}
