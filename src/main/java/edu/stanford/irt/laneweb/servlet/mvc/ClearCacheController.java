package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.Serializable;

import javax.cache.Cache;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.cocoon.cache.CachedResponse;

@Controller
public class ClearCacheController {

    private Cache<Serializable, CachedResponse> cache;

    public ClearCacheController(final Cache<Serializable, CachedResponse> cache) {
        this.cache = cache;
    }

    @GetMapping(value = "/secure/admin/clearcache")
    @ResponseBody
    public String clearCache() {
        this.cache.clear();
        return "OK";
    }
}
