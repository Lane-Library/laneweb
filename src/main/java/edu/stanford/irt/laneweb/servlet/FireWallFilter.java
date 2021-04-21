package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.util.function.Predicate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@WebFilter("/*")
public class FireWallFilter extends AbstractLanewebFilter {

  private StrictHttpFirewall firewall;

  private Predicate<String> forbiddenValue = n -> !n.contains("\0");

  public FireWallFilter() {
    this.firewall = new StrictHttpFirewall();
    this.firewall.setAllowedParameterValues(forbiddenValue);
  }

  @Override
  protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    try {
      FirewalledRequest req = this.firewall.getFirewalledRequest(request);
      chain.doFilter(req, response);
    } catch (RequestRejectedException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
}
