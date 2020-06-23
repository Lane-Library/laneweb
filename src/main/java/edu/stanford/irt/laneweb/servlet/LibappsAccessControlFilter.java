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
    @Qualifier("java.net.URI/libguide-service") 
    private URI libguideServiceURI;

    @Autowired
    @Qualifier("java.net.URI/libcal-service") 
    private URI libcalServiceURI;
    
    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if( this.libguideServiceURI.getHost().equals(request.getRemoteHost())){
            response.addHeader("Access-Control-Allow-Origin", this.libguideServiceURI.getScheme()+ "://" + this.libguideServiceURI.getHost());
        }else {
            response.addHeader("Access-Control-Allow-Origin", this.libcalServiceURI.getScheme()+ "://" + this.libcalServiceURI.getHost());
        }   
        chain.doFilter(request, response);
    }
}
