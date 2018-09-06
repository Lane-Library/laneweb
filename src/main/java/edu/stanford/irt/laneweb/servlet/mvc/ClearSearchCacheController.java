package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

@Controller
public class ClearSearchCacheController {

    private MetaSearchService metaSearchService;

    public ClearSearchCacheController(
            final MetaSearchService metaSearchService) {
        this.metaSearchService = metaSearchService;
    }

    @GetMapping(value = "/apps/search/clearcache")
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
