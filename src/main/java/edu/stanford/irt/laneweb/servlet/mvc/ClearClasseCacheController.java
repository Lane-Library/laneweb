package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
    public void clearClassCache(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String contentBase = request.getAttribute(Model.CONTENT_BASE).toString();
        PipelineCacheKey key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes-aggregator",
                "apps:/classes/onlineregistrationcenter.xml"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes",
                "http://onlineregistrationcenter.com/registerlistxml.asp?p=2&dsd=0&s=4&m=257"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "html", contentBase
                .concat("/includes/classes-thisweek.html")));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "xinclude", "XInclude"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/rss2html.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "html", contentBase
                .concat("/classes-consult/laneclasses.html")));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "xinclude", "XInclude"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/rss2html.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes", "apps:/classes/data.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/classes-thisweek.xsl"));
        key.addKey(new ComponentCacheKey(
                ComponentCacheKey.ComponentType_Serializer,
                "xhtml",
                ";doctype-public=-//W3C//DTD XHTML 1.0 Strict//EN;doctype-system=http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd;encoding=UTF-8;indent=yes;omit-xml-declaration=yes"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes", "apps:/classes/data.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/laneclasses.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "classes", "apps:/classes/data.xml"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/classes-thisweek.xsl"));
        this.cache.remove(key);
        key = new PipelineCacheKey();
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Generator, "html", contentBase.concat("index.html")));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "xinclude", "XInclude"));
        key.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Transformer, "saxon",
                "jndi:/localhost/resources/xsl/rss2html.xsl"));
        this.cache.remove(key);
        this.servletContext.getRequestDispatcher("/classes-consult/laneclasses.html").forward(request, response);
    }
}
