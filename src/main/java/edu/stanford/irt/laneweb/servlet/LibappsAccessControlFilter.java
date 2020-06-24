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
import org.springframework.beans.factory.annotation.Value;

@WebFilter({"/libapps/lane-guides-menu.html", "/classes/libcal-menu.xml"})
public class LibappsAccessControlFilter extends AbstractLanewebFilter {

    @Autowired
    @Qualifier("java.net.URI/libguide-service") 
    private URI libguideServiceURI;

    @Value("${edu.stanford.irt.laneweb.libcal.host}")
    private String libcalHost;
    
    @Value("${edu.stanford.irt.laneweb.libcal.scheme}")
    private String scheme;
    
    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if( this.libguideServiceURI.getHost().equals(request.getRemoteHost())){
            response.addHeader("Access-Control-Allow-Origin", this.libguideServiceURI.getScheme()+ "://" + this.libguideServiceURI.getHost());
        }else {
            response.addHeader("Access-Control-Allow-Origin", scheme+"://"+libcalHost);
        }   
        chain.doFilter(request, response);
    }
}
