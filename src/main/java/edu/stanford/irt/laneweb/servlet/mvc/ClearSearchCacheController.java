package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.search.MetaSearchManagerSource;
import edu.stanford.irt.search.spring.SearchCacheManager;

@Controller
public class ClearSearchCacheController {

    private SearchCacheManager searchCache;

    @RequestMapping(value = "/apps/search/clearcache")
    @ResponseBody
    public String clearCache(@RequestParam(required = false) final String q) {
        if (q != null) {
            this.searchCache.clearCache(q);
        } else {
            this.searchCache.clearAllCaches();
        }
        return "OK";
    }

    @Autowired
    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        if (null == msms) {
            throw new IllegalArgumentException("null metaSearchManagerSource");
        }
        this.searchCache = msms.getSearchCacheManager();
        if (null == this.searchCache) {
            throw new IllegalStateException("null searchCacheManager");
        }
    }
}
