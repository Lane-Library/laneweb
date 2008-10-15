package edu.stanford.irt.laneweb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

public class UserInfoHelperImpl extends AbstractLogEnabled implements UserInfoHelper, ThreadSafe, Initializable, Serviceable {

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
            if (Affiliation.ERR.equals(affiliation) && getLogger().isErrorEnabled()) {
                getLogger().error("error parsing ip for Affiliation: ".concat(ip));
            }
        }
        if (userInfo.getSunetId() == null) {
            String remoteUser = request.getRemoteUser();
            if (remoteUser != null) {
                userInfo.setSunetId(remoteUser);
                // TODO: find a better way to deal with lack of kerberos
                // credentials in various servers
                try {
                    userInfo.setPerson(this.ldapClient.getLdapPerson(remoteUser));
                } catch (Exception e) {
                    if (getLogger().isErrorEnabled()) {
                        getLogger().error(e.getMessage(), e);
                    }
                }
            }
        }
        if ((null != userInfo.getSunetId()) && (null != this.ezproxyKey)) {
            userInfo.setTicket(new Ticket(userInfo.getSunetId(), this.ezproxyKey));
        }
        if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
            userInfo.setProxyLinks(new Boolean(request.getParameter(LanewebConstants.PROXY_LINKS)));
        }
    }

    public void initialize() throws NamingException {
        Context context = new InitialContext();
        setEzproxyKey((String) context.lookup("java:comp/env/ezproxy-key"));
    }

    void setLdapClient(final LdapClient ldapClient) {
        if (null == ldapClient) {
            throw new IllegalArgumentException("null ldapClient");
        }
        this.ldapClient = ldapClient;
    }

    void setEzproxyKey(final String ezproxyKey) {
        if (null == ezproxyKey) {
            throw new IllegalArgumentException("null ezproxyKey");
        }
        this.ezproxyKey = ezproxyKey;
    }

    public void service(final ServiceManager manager) throws ServiceException {
        setLdapClient((LdapClient) manager.lookup(LdapClient.ROLE));
    }

}
