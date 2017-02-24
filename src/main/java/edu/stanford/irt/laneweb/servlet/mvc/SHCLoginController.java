package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.codec.SHCCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;

@Controller
public class SHCLoginController {

    private static final String AND_ERROR_EQUALS = "&error=";

    private static final String EPIC_PREFIX = "epic-";

    private static final String ERROR_EMRID = "invalid or missing emrid: ";

    private static final String ERROR_MISSING_USER_ID = "missing active userid for univid: ";

    private static final String ERROR_TIMESTAMP = "invalid or missing timestamp: ";

    private static final String ERROR_UNIVID = "invalid or missing univid: ";

    private static final Logger LOG = LoggerFactory.getLogger(SHCLoginController.class);

    private static final int ONE_MINUTE = 1000 * 60;

    private static final String TARGET_URL = "/portals/shc.html?sourceid=shc&u=";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private SHCCodec codec;

    private LDAPDataAccess ldapDataAccess;

    @Autowired
    public SHCLoginController(final SHCCodec codec, final LDAPDataAccess ldapDataAccess) {
        this.codec = codec;
        this.ldapDataAccess = ldapDataAccess;
    }

    @RequestMapping(value = "/shclogin")
    public void login(@RequestParam final String emrid, @RequestParam final String univid,
            @RequestParam final String ts, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        StringBuilder errorMsg = new StringBuilder();
        StringBuilder url = new StringBuilder(TARGET_URL);
        url.append(URLEncoder.encode(emrid, UTF_8));
        String userid = null;
        String decryptedUnivid = null;
        if (!validateTimestamp(ts)) {
            errorMsg.append(ERROR_TIMESTAMP).append(ts);
        } else {
            HttpSession session = request.getSession();
            if (!validateAndPopulateEmrid(emrid, session)) {
                errorMsg.append(ERROR_EMRID).append(emrid);
            }
            if (!validateAndPopulateUnivid(univid, session)) {
                errorMsg.append(ERROR_UNIVID).append(univid);
            } else {
                decryptedUnivid = (String) session.getAttribute(Model.UNIVID);
                userid = getUserId(session, decryptedUnivid);
            }
        }
        if (userid == null && decryptedUnivid != null) {
            errorMsg.append(ERROR_MISSING_USER_ID).append(decryptedUnivid);
        }
        if (errorMsg.length() > 0) {
            url.append(AND_ERROR_EQUALS).append(URLEncoder.encode(errorMsg.toString(), UTF_8));
            if (LOG.isInfoEnabled()) {
                LOG.info(errorMsg.append(" -- emrid:{}, univid:{}, ts:{}").toString(), emrid, univid, ts);
            }
        }
        response.sendRedirect("https://" + request.getServerName() + request.getContextPath() + url.toString());
    }

    private String getUserId(final HttpSession session, final String univid) {
        String userid = (String) session.getAttribute(Model.USER_ID);
        if (userid == null) {
            userid = this.ldapDataAccess.getActiveSunetId(univid);
            if (userid != null) {
                userid = userid + "@stanford.edu";
                session.setAttribute(Model.USER_ID, userid);
            }
        }
        return userid;
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
            LOG.error("error parsing " + timestamp, e);
            return false;
        }
        Date now = new Date();
        if (Math.abs(now.getTime() - decryptedTimestamp) < ONE_MINUTE) {
            return true;
        }
        LOG.error("invalid timestamp -- now: {}, timestamp: {}", now.getTime(), decryptedTimestamp);
        return false;
    }
}
