package edu.stanford.irt.laneweb.cookieConverter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.stanford.irt.laneweb.servlet.AbstractLanewebFilter;

@WebFilter("/*")
public class UserCookieUpdatorFilter extends AbstractLanewebFilter {

    @Autowired
    SourceCookieHelper sourceCookieHelper;

    @Autowired
    TargetCookieHelper targetCookieHelper;

    private static final Logger log = LoggerFactory.getLogger(UserCookieUpdatorFilter.class);

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // In tomcat, war configuration the @Autowired doesn't work.
        if (this.sourceCookieHelper != null) {
            Cookie oldCookie = this.sourceCookieHelper.getOldUserCookies(request);
            if (oldCookie != null) {
                try {
                    String sourceCookieValue = this.sourceCookieHelper.getCookieValue(oldCookie, request.getHeader("User-Agent"));
                    log.info("Cookie converter --> {} ", sourceCookieValue);
                    this.targetCookieHelper.setCookies(request, response, sourceCookieValue);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    this.sourceCookieHelper.deleteOldUserCookie(response);
                    String url = getUrl(request);
                    response.sendRedirect(url);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private String getUrl(HttpServletRequest req) {
        String queryString = req.getQueryString();
        return queryString == null ? req.getRequestURL().toString()
                : req.getRequestURL().append('?').append(queryString).toString();
    }
}
