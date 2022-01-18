package edu.stanford.irt.laneweb.cookie;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.AbstractLanewebFilter;
import edu.stanford.irt.laneweb.servlet.binding.user.CookieUserFactory;
import edu.stanford.irt.laneweb.user.User;

@WebFilter("/*")
public class UserCookieUpdatorFilter extends AbstractLanewebFilter {
    
    @Autowired
    CookieHelper cookieHelper;
    
    @Value("${edu.stanford.irt.laneweb.useridcookiecodec.oldkey}")
    String oldUserCookieKey;

    @Value("${edu.stanford.irt.laneweb.useridhasholdkey}")
    String oldUserIdHashKey;

     CookieUserFactory oldCookieUserFactory;


    @PostConstruct
    public void setOldUerCookieCodec() {
        UserCookieCodec oldCodec = new UserCookieCodec(this.oldUserCookieKey);
        this.oldCookieUserFactory = new CookieUserFactory(oldCodec, this.oldUserIdHashKey);
    }

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (this.cookieHelper.isOldUserCookiesPresent(request)) {
            User user = this.oldCookieUserFactory.createUser(request);
            this.cookieHelper.setCookies(request, response, user);
            this.cookieHelper.resetOldUserCookies(response);
        }
        chain.doFilter(request, response);
    }
}
