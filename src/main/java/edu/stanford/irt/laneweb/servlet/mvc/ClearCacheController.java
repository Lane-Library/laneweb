package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.caching.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClearCacheController {

    @Autowired
    private Cache cache;
    
    
    @RequestMapping(value = "/secure/admin/clearcache")
    @ResponseBody
    public String clearCache(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.cache.clear();
        return "OK";
    }
}
