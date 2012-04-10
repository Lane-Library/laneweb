package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.environment.http.HttpResponse;


public class TobaccoRedirectFilter extends AbstractLanewebFilter {

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.setStatus(HttpResponse.SC_MOVED_PERMANENTLY);
        response.addHeader("Location", "http://tobacco.stanford.edu/");
    }
}
