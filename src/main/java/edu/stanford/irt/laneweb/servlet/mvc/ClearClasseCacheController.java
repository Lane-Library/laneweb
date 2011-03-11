package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.ComponentCacheKey;
import org.apache.cocoon.caching.PipelineCacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.model.Model;

@Controller
public class ClearClasseCacheController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private Cache cache;

    @RequestMapping(value = "/secure/classes/reload.html")
    public void clearClassCache(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        resetCache(request.getAttribute(Model.CONTENT_BASE).toString());
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            String cookieValue = cookie.getValue();
            if (cookieValue.endsWith("JSESSIONID")) {
                if (cookieValue.endsWith("tc-laneweb-08"))
                    cookieValue = cookieValue.replace("tc-laneweb-08", "tc-laneweb-07");
                else
                    cookieValue = cookieValue.replace("tc-laneweb-07", "tc-laneweb-08");
                cookie.setValue(cookieValue);
                response.addCookie(cookie);
            }
        }
        response.sendRedirect("https://".concat(request.getServerName()).concat("/secure/classes/reloadAll.html"));
    }

    @RequestMapping(value = "/secure/classes/reloadAll.html")
    public void clearAllClassCache(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        resetCache(request.getAttribute(Model.CONTENT_BASE).toString());
        this.servletContext.getRequestDispatcher("/classes-consult/laneclasses.html").forward(request, response);
    }

    private void resetCache(String contentBase) {
        PipelineCacheKey key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes-aggregator",
                "apps:/classes/onlineregistrationcenter.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/classes/lastmodified.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes",
                "http://onlineregistrationcenter.com/registerlistxml.asp?p=2&dsd=0&s=4&m=257"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes-aggregator",
                "apps:/classes/all-onlineregistrationcenter.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/classes/lastmodified.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes",
                "http://onlineregistrationcenter.com/registerlistxml.asp?p=2&dsd=0&s=4&m=257&amp;dall=1"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes", "apps:/classes/data.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/classes/laneclasses.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "html", contentBase
                .concat("/classes-consult/laneclasses.html")));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "xinclude", "XInclude"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "html", contentBase
                .concat("/includes/classes-thisweek.html")));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "xinclude", "XInclude"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes", "apps:/classes/data.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/classes/thisweek.xsl"));
        this.cache.remove(key);
    }
}
