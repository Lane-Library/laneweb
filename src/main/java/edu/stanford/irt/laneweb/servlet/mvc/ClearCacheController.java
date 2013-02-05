package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.cocoon.cache.Cache;

@Controller
public class ClearCacheController {

    @Autowired
    private Cache cache;

    @RequestMapping(value = "/secure/admin/clearcache")
    @ResponseBody
    public String clearCache() {
        this.cache.clear();
        return "OK";
    }
}
