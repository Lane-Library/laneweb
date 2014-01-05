package edu.stanford.irt.laneweb.integration;

import javax.naming.NamingException;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

public class LanewebContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        JdbcDataSource ds = new JdbcDataSource();
        try {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:comp/env/jdbc/eresources", ds);
            builder.bind("java:comp/env/jdbc/voyager", ds);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("laneweb.context.ezproxy-key", "key");
        System.setProperty("laneweb.sunetidhashkey", "key");
        System.setProperty("laneweb.shccodec.key", "key");
        System.setProperty("laneweb.shccodec.vector", "key");
        System.setProperty("laneweb.sunetidcookiecodec.key", "key");
        System.setProperty("laneweb.context.live-base", "file:/Users/ceyates/Documents/workspace/laneweb-content-svn");
        System.setProperty("laneweb.context.stage-base", "file:/Users/ceyates/Documents/workspace/laneweb-content-svn");
        System.setProperty("laneweb.context.version", "foo");
        System.setProperty("laneweb.disaster-mode", "false");
        MockServletContext servletContext = (MockServletContext) ((WebApplicationContext) applicationContext)
                .getServletContext();
        servletContext.setInitParameter("laneweb.context.version", "version");
    }
}