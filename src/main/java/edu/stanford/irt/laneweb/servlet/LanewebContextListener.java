package edu.stanford.irt.laneweb.servlet;

import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Creates URL to various content locations as well as some key Strings and adds them as ServletContext attributes. It
 * first looks at System properties for the values. If any are not System properties they are looked for in context init
 * parameters, if not there they are looked up up using JNDI.
 * 
 * @author ceyates $Id$
 */
public class LanewebContextListener implements ServletContextListener {

    private static final String EZPROXY_KEY = "ezproxy-key";

    private static final String LIVE_BASE_KEY = "laneweb.context.live-base";

    private static final String MEDBLOG_BASE_KEY = "laneweb.context.medblog-base";

    private static final String STAGE_BASE_KEY = "laneweb.context.stage-base";

    private static final String VERSION_KEY = "laneweb.context.version";

    private static final String WEBDASH_KEY = "webdash-key";

    private Context namingContext;

    private ServletContext servletContext;

    public void contextDestroyed(final ServletContextEvent sce) {
    }

    public void contextInitialized(final ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        this.servletContext.setAttribute(LIVE_BASE_KEY, getURL(LIVE_BASE_KEY));
        this.servletContext.setAttribute(STAGE_BASE_KEY, getURL(STAGE_BASE_KEY));
        this.servletContext.setAttribute(MEDBLOG_BASE_KEY, getURL(MEDBLOG_BASE_KEY));
        this.servletContext.setAttribute(WEBDASH_KEY, getValue(WEBDASH_KEY));
        this.servletContext.setAttribute(EZPROXY_KEY, getValue(EZPROXY_KEY));
        this.servletContext.setAttribute(VERSION_KEY, getValue(VERSION_KEY));
    }

    /**
     * get a url associated with a name, delegating to getValue() above.
     * 
     * @param name
     * @return a URL associated with the name
     */
    private URL getURL(final String name) {
        try {
            return new URL(getValue(name));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("unable to determine URL for '" + name + "'", e);
        }
    }

    /**
     * get a System property associated with a name, failing that get a context init parameter and failing that a jndi
     * object, and failing that, die.
     * 
     * @param name
     * @return The property or jndi value
     */
    private String getValue(final String name) {
        String value = System.getProperty(name);
        if (null == value) {
            value = this.servletContext.getInitParameter(name);
        }
        if (null == value) {
            try {
                if (null == this.namingContext) {
                    this.namingContext = new InitialContext();
                }
                value = (String) this.namingContext.lookup("java:comp/env/" + name);
            } catch (NamingException e) {
                throw new IllegalStateException("unable to determine value for '" + name + "'", e);
            }
        }
        return value;
    }
}
