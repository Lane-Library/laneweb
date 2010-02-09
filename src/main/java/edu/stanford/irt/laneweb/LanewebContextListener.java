package edu.stanford.irt.laneweb;

import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Creates URL to various content locations as well as some key Strings and adds them as
 * ServletContext attributes. It first looks at System properties for the values. If any
 * are not System properties they are looked up using JNDI.
 * 
 * @author ceyates
 */
public class LanewebContextListener implements ServletContextListener {

    private static final String LIVE_BASE_KEY = "laneweb.context.live-base";

    private static final String MEDBLOG_BASE_KEY = "laneweb.context.medblog-base";

    private static final String STAGE_BASE_KEY = "laneweb.context.stage-base";
    
    private static final String WEBDASH_KEY = "laneweb.context.webdash-key";
    
    private static final String EZPROXY_KEY = "laneweb.context.ezproxy-key";

    private Context context;

    public void contextDestroyed(final ServletContextEvent sce) {
    }

    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(LIVE_BASE_KEY, getURL(LIVE_BASE_KEY));
        context.setAttribute(STAGE_BASE_KEY, getURL(STAGE_BASE_KEY));
        context.setAttribute(MEDBLOG_BASE_KEY, getURL(MEDBLOG_BASE_KEY));
        context.setAttribute(WEBDASH_KEY, getValue(WEBDASH_KEY));
        context.setAttribute(EZPROXY_KEY, getValue(EZPROXY_KEY));
    }

    private String getValue(String name) {
        String value = System.getProperty(name);
        if (null == value) {
            try {
                if (null == this.context) {
                    this.context = new InitialContext();
                }
                value = (String) this.context.lookup("java:comp/env/" + name);
            } catch (NamingException e) {
                throw new IllegalStateException("unable to determine value for '" + name + "'", e);
            }
        }
        return value;
    }

    private URL getURL(final String name) {
        try {
            return new URL(getValue(name));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("unable to determine URL for '" + name + "'", e);
        }
    }
}
