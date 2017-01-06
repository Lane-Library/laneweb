package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.metasearch.MetaSearchManagerSource;

@Controller
public class ClearSearchCacheController {

    private MetaSearchManagerSource msms;

    @Autowired
    public ClearSearchCacheController(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    @RequestMapping(value = "/apps/search/clearcache")
    @ResponseBody
    public String clearCache(@RequestParam(required = false) final String q) {
        if (q != null) {
            this.msms.getSearchCacheManager().clearCache(q);
        } else {
            this.msms.getSearchCacheManager().clearAllCaches();
        }
        return "OK";
    }
}
