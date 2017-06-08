package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

@Controller
public class ClearSearchCacheController {

    private MetaSearchService metaSearchService;

    @Autowired
    public ClearSearchCacheController(final MetaSearchService metaSearchService) {
        this.metaSearchService = metaSearchService;
    }

    @RequestMapping(value = "/apps/search/clearcache")
    @ResponseBody
    public String clearCache(@RequestParam(required = false) final String q) {
        if (q != null) {
            this.metaSearchService.clearCache(q);
        } else {
            this.metaSearchService.clearAllCaches();
        }
        return "OK";
    }
}
