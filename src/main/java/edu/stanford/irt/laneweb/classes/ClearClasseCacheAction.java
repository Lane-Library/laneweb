package edu.stanford.irt.laneweb.classes;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.ComponentCacheKey;
import org.apache.cocoon.caching.PipelineCacheKey;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

public class ClearClasseCacheAction implements Action {

    private Cache cache;

    public ClearClasseCacheAction(final Cache cache) {
        this.cache = cache;
    }

    @SuppressWarnings("rawtypes")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters parameters) {
        String contentBase = ((URL) objectModel.get("content-base")).toString();
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
        return Collections.emptyMap();
    }
}
