package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SHCCodec;

@Controller
public class SHCLoginController {

    private static final String AND_ERROR_EQUALS = "&error=";

    private static final String EPIC_PREFIX = "epic-";

    private static final String ERROR_EMRID = "invalid or missing emrid: ";

    private static final String ERROR_MISSING_SUNETID = "missing active sunetid for univid: ";

    private static final String ERROR_TIMESTAMP = "invalid or missing timestamp: ";

    private static final String ERROR_UNIVID = "invalid or missing univid: ";

    private static final int ONE_MINUTE = 1000 * 60;

    private static final String TARGET_URL = "/portals/shc.html?sourceid=shc&u=";

    private SHCCodec codec;

    private LDAPDataAccess ldapDataAccess;

    private final Logger log;

    @Autowired
    public SHCLoginController(final SHCCodec codec, final LDAPDataAccess ldapDataAccess, @Qualifier(value="org.slf4j.Logger/SHCLoginController") final Logger log) {
        this.codec = codec;
        this.ldapDataAccess = ldapDataAccess;
        this.log = log;
    }

    @RequestMapping(value = "/shclogin")
    public void login(@RequestParam final String emrid, @RequestParam final String univid, @RequestParam final String ts,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        StringBuffer errorMsg = new StringBuffer();
        StringBuffer url = new StringBuffer(TARGET_URL);
        url.append(URLEncoder.encode(emrid, "UTF-8"));
        String sunetid = null;
        String decryptedUnivid = null;
        if (!validateTimestamp(ts)) {
            errorMsg.append(ERROR_TIMESTAMP + ts);
        } else {
            if (!validateAndPopulateEmrid(emrid, session)) {
                errorMsg.append(ERROR_EMRID + emrid);
            }
            if (!validateAndPopulateUnivid(univid, session)) {
                errorMsg.append(ERROR_UNIVID + univid);
            } else {
                decryptedUnivid = (String) session.getAttribute(Model.UNIVID);
                sunetid = getSunetid(session, decryptedUnivid);
            }
        }
        if (sunetid == null && decryptedUnivid != null) {
            errorMsg.append(ERROR_MISSING_SUNETID + decryptedUnivid);
        }
        if (errorMsg.length() > 0) {
            this.log.info(errorMsg.toString() + " -- emrid:" + emrid + ", univid:" + univid + ", ts:" + ts);
            url.append(AND_ERROR_EQUALS).append(URLEncoder.encode(errorMsg.toString(), "UTF-8"));
        }
        response.sendRedirect("https://" + request.getServerName() + request.getContextPath() + url.toString());
    }

    private String getSunetid(final HttpSession session, final String univid) {
        String sunetid = (String) session.getAttribute(Model.SUNETID);
        if (sunetid == null) {
            LDAPData ldapData = this.ldapDataAccess.getLdapDataForUnivid(univid);
            sunetid = (ldapData.isActive()) ? ldapData.getSunetId() : null;
            if (sunetid != null) {
                session.setAttribute(Model.SUNETID, sunetid);
            }
        }
        return sunetid;
    }

    private boolean validateAndPopulateEmrid(final String emrid, final HttpSession session) {
        String decryptedEmrid = this.codec.decrypt(emrid);
        if (null != decryptedEmrid && !decryptedEmrid.isEmpty()) {
            session.setAttribute(Model.EMRID, EPIC_PREFIX + decryptedEmrid);
            return true;
        }
        return false;
    }

    private boolean validateAndPopulateUnivid(final String univid, final HttpSession session) {
        String decryptedUnivid = this.codec.decrypt(univid);
        if (null != decryptedUnivid && !decryptedUnivid.isEmpty()) {
            session.setAttribute(Model.UNIVID, decryptedUnivid);
            return true;
        }
        return false;
    }

    private boolean validateTimestamp(final String timestamp) {
        long decryptedTimestamp;
        try {
            decryptedTimestamp = Long.parseLong(this.codec.decrypt(timestamp));
        } catch (NumberFormatException e) {
            this.log.error("error parsing " + timestamp, e);
            return false;
        }
        Date now = new Date();
        if (Math.abs(now.getTime() - decryptedTimestamp) < ONE_MINUTE) {
            return true;
        }
        this.log.error("invalid timestamp -- now: " + now.getTime() + ", timestamp: " + decryptedTimestamp);
        return false;
    }
}
