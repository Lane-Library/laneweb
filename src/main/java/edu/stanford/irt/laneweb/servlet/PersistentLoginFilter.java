package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistentLoginFilter implements Filter {

    /**
     * this codec codes and decodes the cookie value using sunet id, useragent
     * and time of creation
     */
    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    private SunetIdSource sunetIdSource = new SunetIdSource();
    
    private Logger log = LoggerFactory.getLogger("cookies");

    public void destroy() {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
            ServletException {
        String userAgent = ((HttpServletRequest) request).getHeader("user-agent");
        boolean isIphone = userAgent != null && userAgent.indexOf("iPhone") > -1;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (Boolean.parseBoolean(request.getParameter(("pl")))) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String sunetid = this.sunetIdSource.getSunetid(httpRequest);
            if (sunetid != null) {
                if (isIphone) {
                    this.log.info("adding persistent login cookie");
                }
                setLoginCookie(sunetid, httpRequest, httpResponse);
            }
        } else if (Boolean.parseBoolean(request.getParameter("remove-pl"))) {
            if (isIphone) {
                this.log.info("removing persistent login cookie");
            }
            removeLoginCookie(httpResponse);
        }
        chain.doFilter(request, response);
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    /**
     * set the lane-user cookie max age to zero.
     * 
     * @param response
     */
    private void removeLoginCookie(final HttpServletResponse response) {
        Cookie cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * create and set the lane-user cookie
     * 
     * @param sunetid
     * @param request
     * @param response
     */
    private void setLoginCookie(final String sunetid, final HttpServletRequest request, final HttpServletResponse response) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent) {
            PersistentLoginToken token = this.codec.createLoginToken(sunetid, userAgent.hashCode());
            Cookie cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 7 * 2); // cookie is available for 2
                                                 // weeks
            response.addCookie(cookie);
        }
    }
}
