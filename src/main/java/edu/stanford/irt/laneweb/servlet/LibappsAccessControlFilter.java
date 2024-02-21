package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.URI;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@WebFilter( urlPatterns = {"/libguides/*", "/libcals/libcal-menu.html", "/apps/suggest/getSuggestionList" })
public class LibappsAccessControlFilter extends AbstractLanewebFilter {

    @Autowired
    @Qualifier("java.net.URI/libguide-service")
    private URI libguideServiceURI;

    @Value("${edu.stanford.irt.laneweb.libcal.host}")
    private String libcalHost;

    @Value("${edu.stanford.irt.laneweb.libcal.scheme}")
    private String libcalScheme;

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String libguideURI = this.libguideServiceURI.getScheme() + "://" + this.libguideServiceURI.getHost();
        String libcalURI = this.libcalScheme + "://" + this.libcalHost;
        String origin = request.getHeader("origin");
        if (libguideURI.equals(origin) || libcalURI.equals(origin)) {
            response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
            response.addHeader("Access-Control-Allow-Origin", origin);
        }
        chain.doFilter(request, response);
    }
 
    @Override
    public void init(FilterConfig filterConfig) {
      SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,  filterConfig.getServletContext());
    }

 }
