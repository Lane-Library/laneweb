package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.net.URLEncoder;

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

    private static final String AND_ERROR_EQUALS = "&error=";

    private static final String EPIC_PREFIX = "epic-";

    private static final String ERROR_1 = "missing emrid; ";

    private static final String ERROR_2 = "no univid from shc; ";

    private static final String ERROR_3 = "missing sunetid for univid: ";

    private static final String TARGET_URL = "/portals/shc.html?sourceid=shc";

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
        StringBuffer errorMsg = new StringBuffer();
        String url = TARGET_URL;
        String sunetid = null;
        String decryptedEmrid = null;
        String decryptedUnivid = null;
        decryptedEmrid = this.codec.decrypt(emrid);
        decryptedUnivid = this.codec.decrypt(univid);
        if (null == decryptedEmrid || decryptedEmrid.isEmpty()){
            errorMsg.append(ERROR_1);
            decryptedEmrid = null;
        }
        if (null == decryptedUnivid || decryptedUnivid.isEmpty()) {
            errorMsg.append(ERROR_2);
            decryptedUnivid = null;
        }
        if (decryptedEmrid != null) {
            session.setAttribute(Model.EMRID, EPIC_PREFIX + decryptedEmrid);
        }
        if (decryptedUnivid != null) {
            session.setAttribute(Model.UNIVID, decryptedUnivid);
            sunetid = getSunetid(session, decryptedUnivid);
        }
        if (sunetid == null && decryptedUnivid != null) {
            errorMsg.append(ERROR_3 + decryptedUnivid);
        }
        if (!errorMsg.toString().isEmpty()) {
            this.log.error(errorMsg.toString());
            url += AND_ERROR_EQUALS + URLEncoder.encode(errorMsg.toString(), "UTF-8");
        }
        response.sendRedirect(request.getContextPath() + url);
    }
}
