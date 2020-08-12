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

@WebFilter( urlPatterns = {"/content/*", "/classes/libcal-menu.html" })
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
            response.addHeader("Access-Control-Allow-Origin", origin);
        }
        chain.doFilter(request, response);
    }
}
