package edu.stanford.irt.laneweb;

import javax.servlet.http.HttpSession;

import org.apache.cocoon.environment.Request;
import org.apache.log4j.Logger;

public class UserInfoHelper {

    public static final String ROLE = UserInfoHelper.class.getName();

    private Logger logger = Logger.getLogger(UserInfoHelper.class);

    private LdapClient ldapClient;

    private String ezproxyKey;

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
            setUserInfo(userInfo, request);
        }
        return userInfo;
    }

    private void setUserInfo(final UserInfo userInfo, final Request request) {
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
        if (userInfo.getSunetId() == null) {
            String remoteUser = request.getRemoteUser();
            if (remoteUser != null) {
                userInfo.setSunetId(remoteUser);
                userInfo.setPerson(this.ldapClient.getLdapPerson(remoteUser));
            }
        }
        if ((null != userInfo.getSunetId()) && (null != this.ezproxyKey)) {
            userInfo.setTicket(new Ticket(userInfo.getSunetId(), this.ezproxyKey));
        }
        if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
            userInfo.setProxyLinks(new Boolean(request.getParameter(LanewebConstants.PROXY_LINKS)));
        }
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
