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
 * Creates URL to various content locations as well as some key Strings and adds
 * them as ServletContext attributes. It first looks at System properties for
 * the values. If any are not System properties they are looked for in context
 * init parameters, if not there they are looked up up using JNDI.
 */
public class LanewebContextListener implements ServletContextListener {

    public static final String EZPROXY = "laneweb.context.ezproxy-key";

    public static final String LIVE_BASE = "laneweb.context.live-base";

    public static final String MEDBLOG_BASE = "laneweb.context.medblog-base";

    public static final String STAGE_BASE = "laneweb.context.stage-base";

    public static final String VERSION = "laneweb.context.version";

    private Context namingContext;

    private ServletContext servletContext;

    public void contextDestroyed(final ServletContextEvent sce) {
    }

    public void contextInitialized(final ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        this.servletContext.setAttribute(LIVE_BASE, getURL(LIVE_BASE));
        this.servletContext.setAttribute(STAGE_BASE, getURL(STAGE_BASE));
        this.servletContext.setAttribute(MEDBLOG_BASE, getURL(MEDBLOG_BASE));
        this.servletContext.setAttribute(EZPROXY, getValue(EZPROXY));
        this.servletContext.setAttribute(VERSION, getValue(VERSION));
    }

    /**
     * get a url associated with a name, delegating to getValue() above.
     * 
     * @param name
     * @return a URL associated with the name
     */
    private URL getURL(final String name) {
        String nameWithSlash = name.endsWith("/") ? name : name + "/";
        try {
            return new URL(getValue(nameWithSlash));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("unable to determine URL for '" + nameWithSlash + "'", e);
        }
    }

    /**
     * get a System property associated with a name, failing that get a context
     * init parameter and failing that a jndi object, and failing that, die.
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
