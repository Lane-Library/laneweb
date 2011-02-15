package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.caching.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ClearCacheController {

    @Autowired
    private Cache cache;
    
    @Autowired
    private ServletContext servletContext;
    
    @RequestMapping(value = "/secure/admin/clearcache")
    public void clearCache(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.cache.clear();
        this.servletContext.getRequestDispatcher("/secure/admin/status").forward(request, response);
    }
}
