package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

@Controller
public class ClearSearchCacheController {

    private MetaSearchService metaSearchService;

    @Autowired
    public ClearSearchCacheController(final MetaSearchService msms) {
        this.metaSearchService = msms;
    }

    @RequestMapping(value = "/apps/search/clearcache")
    @ResponseBody
    public String clearCache(@RequestParam(required = false) final String q) {
        if (q != null) {
            this.metaSearchService.clearCache(new SimpleQuery(q));
        } else {
            this.metaSearchService.clearAllCaches();
        }
        return "OK";
    }
}
