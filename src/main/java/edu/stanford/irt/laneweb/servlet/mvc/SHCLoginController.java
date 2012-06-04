package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SHCCodec;

@Controller
public class SHCLoginController {

    private static final String ERROR_URL = "/shc-login-error.html?sourceid=shc";

    private static final String SUCCESS_URL = "/portals/shc.html?sourceid=shc";

    @Autowired
    private SHCCodec codec;

    @Autowired
    private LDAPDataAccess ldapDataSource;

    private final Logger log = LoggerFactory.getLogger(SHCLoginController.class);

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
        decryptedEmrid = this.codec.decrypt(emrid);
        decryptedUnivid = this.codec.decrypt(univid);
        if(decryptedEmrid.isEmpty()){
            decryptedEmrid = null;
        }
        if(decryptedUnivid.isEmpty()){
            decryptedUnivid = null;
        }
        if (decryptedEmrid != null) {
            session.setAttribute(Model.EMRID, "epic-" + decryptedEmrid);
        }
        if (decryptedUnivid != null) {
            session.setAttribute(Model.UNIVID, decryptedUnivid);
            sunetid = getSunetid(session, decryptedUnivid);
        }
        if (sunetid != null) {
            url = SUCCESS_URL;
        } else if (decryptedUnivid != null) {
            this.log.error("missing sunetid for univid: " + decryptedUnivid);
        }
        response.sendRedirect(request.getContextPath() + url);
    }
}
