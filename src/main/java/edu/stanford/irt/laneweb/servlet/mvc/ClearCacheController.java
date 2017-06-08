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

    @Autowired
    private Cache<Serializable, CachedResponse> cache;

    @RequestMapping(value = "/admin/clearcache")
    @ResponseBody
    public String clearCache() {
        this.cache.clear();
        return "OK";
    }
}
