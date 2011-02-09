package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String WEBAUTH_COOKIE_NAME = "webauth_at";

    private static final String WEBAUTH_LOGOUT_URL = "https://weblogin.stanford.edu/logout";
    
    private Logger log = LoggerFactory.getLogger("cookies");

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String userAgent = req.getHeader("user-agent");
        boolean isIphone = userAgent != null && userAgent.indexOf("iPhone") > -1;
        if (isIphone) {
            this.log.info("logout servlet removing cookies:");
        }
        for (Cookie cookie : req.getCookies()) {
            String name = cookie.getName();
            if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(name) || WEBAUTH_COOKIE_NAME.equals(name)) {
                if (isIphone) {
                    this.log.info("removing cookie: " + cookie.getName() + " value: " + cookie.getValue());
                }
                cookie.setValue(null);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
        }
        OutputStream out =  resp.getOutputStream();
        resp.setContentType ("text/html");

        out.write(page.getBytes());
        out.flush();
    }
    
    String page = "<html><head><meta http-equiv=\"refresh\" content=\"0;url=".concat(WEBAUTH_LOGOUT_URL).concat("\"></head></html>");
}
