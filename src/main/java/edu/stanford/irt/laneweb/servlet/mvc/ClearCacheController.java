package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.Serializable;

import javax.cache.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.cocoon.cache.CachedResponse;

@Controller
public class ClearCacheController {

    private Cache<Serializable, CachedResponse> cache;

    @Autowired
    public ClearCacheController(final Cache<Serializable, CachedResponse> cache) {
        this.cache = cache;
    }

    @RequestMapping(value = "/secure/admin/clearcache")
    @ResponseBody
    public String clearCache() {
        this.cache.clear();
        return "OK";
    }
}
