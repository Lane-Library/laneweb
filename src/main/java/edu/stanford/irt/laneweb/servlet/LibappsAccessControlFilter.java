package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.URI;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebFilter("/libapps/lane-guides-menu.html")
public class LibappsAccessControlFilter extends AbstractLanewebFilter {

    @Autowired
    @Qualifier("java.net.URI/libapps-service") 
    private URI libappsServiceURI;
    
    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", this.libappsServiceURI.getScheme()+ "://" + this.libappsServiceURI.getHost());
        chain.doFilter(request, response);
    }
}
