package edu.stanford.irt.laneweb.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;


public class WebAuthAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Object webauthUser = super.getPreAuthenticatedPrincipal(request);
        return "(null)".equals(webauthUser) ? null : webauthUser;
    }
}
