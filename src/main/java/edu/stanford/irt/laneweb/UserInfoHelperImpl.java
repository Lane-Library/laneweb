package edu.stanford.irt.laneweb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

public class UserInfoHelperImpl extends AbstractLogEnabled implements UserInfoHelper, ThreadSafe, Initializable {

    private LdapClient ldapClient;
    
    private String ezproxyKey;

    public UserInfo getUserInfo(final Request request) {

        UserInfo userInfo = (UserInfo) request.getAttribute(LanewebConstants.USER_INFO);
        if (userInfo == null) {
            Session session = request.getSession(true);
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
            // Loa:d balancer also does this
            String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
            if (header != null) {
                ip = header;
            }
            try {
                userInfo.setAffiliation(Affiliation.getAffiliationForIP(ip));
            } catch (Exception e) {
                userInfo.setAffiliation(Affiliation.ERR);
            }
        }
        if (userInfo.getSunetId() == null) {
            String requestSunetId = (String) request.getAttribute(LanewebConstants.WEBAUTH_USER);
            if (requestSunetId != null && !LanewebConstants.UNSET.equals(requestSunetId)) {
                userInfo.setSunetId(requestSunetId);
                userInfo.setLdapPerson(this.ldapClient.getLdapPerson(requestSunetId));
            }
        }
        if (null != userInfo.getSunetId() && null != this.ezproxyKey) {
            userInfo.setTicket(new Ticket(userInfo.getSunetId(), this.ezproxyKey));
        }
        if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
            userInfo.setProxyLinks(new Boolean(request.getParameter(LanewebConstants.PROXY_LINKS)));
        }
    }

    public void initialize() throws NamingException {
        setLdapClient(new LdapClientImpl());
        Context context = new InitialContext();
        this.ezproxyKey = (String) context.lookup("java:comp/env/ezproxy-key");
    }

    void setLdapClient(final LdapClient ldapClient) {
        this.ldapClient = ldapClient;
    }
    
    void setEzproxyKey(final String ezproxyKey) {
        this.ezproxyKey = ezproxyKey;
    }

}
