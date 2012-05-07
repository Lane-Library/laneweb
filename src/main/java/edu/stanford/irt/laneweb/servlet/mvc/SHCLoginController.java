package edu.stanford.irt.laneweb.servlet.mvc;

import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SHCCodec;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SHCLoginController {

    private static final SHCCodec CODEC = new SHCCodec();

    private static final String ERROR_URL = "/shc-login-error.html?sourceid=shc";

    private static final String SUCCESS_URL = "/portals/shc.html?sourceid=shc";

    @Autowired
    private LDAPDataAccess ldapDataSource;

    public String getSunetid(final HttpSession session, final String univid) {
        String sunetid = (String) session.getAttribute(Model.SUNETID);
        if (sunetid == null) {
            sunetid = this.ldapDataSource.getLdapDataForUnivid(univid).getSunetId();
            if (sunetid != null) {
                session.setAttribute(Model.SUNETID, sunetid);
            }
        }
        return sunetid;
    }

    @RequestMapping(value = "/shclogin")
    public void login(@RequestParam final String emrid, @RequestParam final String univid,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String url = ERROR_URL;
        String sunetid = null;
        String decryptedEmrid = null;
        String decryptedUnivid = null;
        if (emrid != null && !emrid.isEmpty()) {
            decryptedEmrid = CODEC.decrypt(emrid);
            session.setAttribute(Model.EMRID, decryptedEmrid);
        }
        if (univid != null && !univid.isEmpty()) {
            decryptedUnivid = CODEC.decrypt(univid);
            session.setAttribute(Model.UNIVID, decryptedUnivid);
            sunetid = getSunetid(session, decryptedUnivid);
        }
        if (sunetid != null) {
            url = SUCCESS_URL;
        }
        response.sendRedirect(request.getContextPath() + url);
    }
}
