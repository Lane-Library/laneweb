package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebFilter("/*")
public class SecureHttpParameterValueFilter extends AbstractLanewebFilter {


  @Override
  protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        SecureHttpParameterValueServletRequest req = new SecureHttpParameterValueServletRequest(request);
        chain.doFilter(req, response);
  }
}
