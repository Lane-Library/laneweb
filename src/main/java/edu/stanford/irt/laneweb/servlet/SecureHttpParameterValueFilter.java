package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebFilter("/*")
public class SecureHttpParameterValueFilter extends AbstractLanewebFilter {


  @Override
  protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        SecureHttpParameterValueServletRequest req = new SecureHttpParameterValueServletRequest(request);
        chain.doFilter(req, response);
  }
}
