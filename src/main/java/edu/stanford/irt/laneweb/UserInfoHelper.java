package edu.stanford.irt.laneweb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.environment.Request;
import org.apache.log4j.Logger;

public class UserInfoHelper {

    public static final String ROLE = UserInfoHelper.class.getName();

    private Logger logger = Logger.getLogger(UserInfoHelper.class);

    private LdapClient ldapClient;

    private String ezproxyKey;

    private Cryptor cryptor;

    public UserInfo getUserInfo(final Request request) {

        UserInfo userInfo = (UserInfo) request.getAttribute(LanewebConstants.USER_INFO);
        if (userInfo == null) {
            HttpSession session = request.getSession(true);
            userInfo = (UserInfo) session.getAttribute(LanewebConstants.USER_INFO);
            if (userInfo == null) {
                userInfo = new UserInfo();
                session.setAttribute(LanewebConstants.USER_INFO, userInfo);
            }
            request.setAttribute(LanewebConstants.USER_INFO, userInfo);

            setUserAffiliation(userInfo, request);
            try {
                setSunetId(userInfo, request);
            } catch (Exception e) {
                this.logger.error(e.getMessage(), e);
            }
            setTicket(userInfo, request);
            setProxyLink(userInfo, request);
        }
        return userInfo;
    }

    private void setUserAffiliation(final UserInfo userInfo, final Request request) {
        if (userInfo.getAffiliation() == null) {
            String ip = request.getRemoteAddr();
            // mod_proxy puts the real remote address in an x-forwarded-for
            // header
            // Load balancer also does this
            String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
            if (header != null) {
                if (header.indexOf(",") > 0) {
                    ip = header.substring(header.lastIndexOf(",") + 1, header.length()).trim();
                } else {
                    ip = header;
                }
            }
            Affiliation affiliation = Affiliation.getAffiliationForIP(ip);
            userInfo.setAffiliation(affiliation);
            if (Affiliation.ERR.equals(affiliation)) {
                this.logger.error("error parsing ip for Affiliation: " + ip);
            }
        }
    }

    private void setSunetId(final UserInfo userInfo, final Request request) throws Exception {
        if (userInfo.getSunetId() == null) {
            String remoteUser = request.getRemoteUser();
            if (remoteUser == null) {
                remoteUser = request.getHeader("x-webauth-user");
            }
            if (remoteUser == null) {
                String cookieSunetid = getSunetId(request);
                if (cookieSunetid != null) {
                    remoteUser = cookieSunetid;
                }
            }
            if (remoteUser != null) {
                userInfo.setSunetId(remoteUser);
                userInfo.setPerson(this.ldapClient.getLdapPerson(remoteUser));
            }
        }
    }

    private void setTicket(final UserInfo userInfo, final Request request) {
        if ((null != userInfo.getSunetId()) && (null != this.ezproxyKey)) {
            userInfo.setTicket(new Ticket(userInfo.getSunetId(), this.ezproxyKey));
        }
    }

    private void setProxyLink(final UserInfo userInfo, final Request request) {
        if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
            userInfo.setProxyLinks(new Boolean(request.getParameter(LanewebConstants.PROXY_LINKS)));
        }
    }

    @SuppressWarnings("unchecked")
    public String getSunetId(final Request request) throws Exception {
        if (this.cryptor == null) {
            throw new RuntimeException("cryptor is null");
        }
        Map<String, Cookie> cookies = request.getCookieMap();
        Cookie sunetIdCookie = cookies.get(LanewebConstants.USER_COOKIE_NAME);
        Cookie securityCookie = cookies.get(LanewebConstants.SECURITY_COOKIE_NAME);
        Cookie dateCookie = cookies.get(LanewebConstants.DATE_COOKIE_NAME);
        if ((null == sunetIdCookie) || (null == securityCookie) || (dateCookie == null)) {
            return null;
        }

        String decryptedSecurity = this.cryptor.decrypt(securityCookie.getValue());
        String decryptedSunetId = this.cryptor.decrypt(sunetIdCookie.getValue());
        String decryptedDate = this.cryptor.decrypt(dateCookie.getValue());

        String userAgent = request.getHeader("User-Agent");
        String comparableSecurity = decryptedDate.concat(decryptedSunetId).concat(userAgent);
        if (!decryptedSecurity.equals(comparableSecurity)) {
            return null;
        }

        GregorianCalendar gc = new GregorianCalendar();
        Date date = new Date(Long.valueOf(decryptedDate).longValue());
        gc.setTime(date);
        gc.add(Calendar.WEEK_OF_YEAR, 2);
        if (gc.before(new GregorianCalendar())) {
            return null;
        }

        return decryptedSunetId;
    }

    public void setCryptor(final Cryptor cryptor) {
        if (null == cryptor) {
            throw new IllegalArgumentException("null cryptor");
        }
        this.cryptor = cryptor;
    }

    public void setLdapClient(final LdapClient ldapClient) {
        if (null == ldapClient) {
            throw new IllegalArgumentException("null ldapClient");
        }
        this.ldapClient = ldapClient;
    }

    public void setEzproxyKey(final String ezproxyKey) {
        if (null == ezproxyKey) {
            throw new IllegalArgumentException("null ezproxyKey");
        }
        this.ezproxyKey = ezproxyKey;
    }

}
