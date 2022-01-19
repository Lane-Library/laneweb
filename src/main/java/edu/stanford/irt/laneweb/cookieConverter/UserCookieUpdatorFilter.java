package edu.stanford.irt.laneweb.cookieConverter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import edu.stanford.irt.laneweb.servlet.AbstractLanewebFilter;
import edu.stanford.irt.laneweb.user.User;

@WebFilter("/*")
public class UserCookieUpdatorFilter extends AbstractLanewebFilter {

    @Autowired
    SourceCookieHelper sourceCookieHelper;
    
    
    @Autowired
    TargetCookieHelper targetCookieHelper;

    

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        //In tomcat, war configuration the @Autowired doesn't work. 
        if (this.sourceCookieHelper != null) {
            Cookie oldCookie = this.sourceCookieHelper.getOldUserCookies(request);
            if (oldCookie != null) {
                User user = this.sourceCookieHelper.getUserFromCookie(oldCookie, request.getHeader("User-Agent"));
                this.targetCookieHelper.setCookies(request, response, user);
                this.sourceCookieHelper.resetOldUserCookies(response);
                String url = getUrl( request);
                response.sendRedirect(url);
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
