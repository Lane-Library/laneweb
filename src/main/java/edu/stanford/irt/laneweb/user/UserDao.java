package edu.stanford.irt.laneweb.user;

import java.security.PrivilegedAction;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.security.auth.Subject;
import javax.security.sasl.Sasl;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;

import edu.stanford.irt.laneweb.Cryptor;
import edu.stanford.irt.laneweb.LanewebConstants;

public class UserDao {

    public static class GSSAPIAuthenticationStrategy implements DirContextAuthenticationStrategy {

        public DirContext processContextAfterCreation(final DirContext ctx, final String userDn, final String password) throws NamingException {
            return ctx;
        }

        @SuppressWarnings("unchecked")
        public void setupEnvironment(final Hashtable env, final String userDn, final String password) throws NamingException {
            env.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
            env.put(Sasl.QOP, "auth-conf");
        }
    }

    private Cryptor cryptor;

    private String ezproxyKey;

    private LdapTemplate ldapTemplate;

    private Logger logger = Logger.getLogger(UserDao.class);

    private SubjectSource subjectSource;

    public User createOrUpdateUser(final HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute(LanewebConstants.USER);
        if (null == user) {
            user = new User();
            session.setAttribute(LanewebConstants.USER, user);
        }
        setUserTrackingAffiliation(user, request);
        setSunetId(user, request);
        setTicket(user, request);
        setProxyLinks(user, request);
        setLdapData(user);
        return user;
    }

    public void setCryptor(final Cryptor cryptor) {
        if (null == cryptor) {
            throw new IllegalArgumentException("null cryptor");
        }
        this.cryptor = cryptor;
    }

    public void setEzproxyKey(final String ezproxyKey) {
        if (null == ezproxyKey) {
            throw new IllegalArgumentException("null ezproxyKey");
        }
        this.ezproxyKey = ezproxyKey;
    }

    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setSubjectSource(final SubjectSource subjectSource) {
        this.subjectSource = subjectSource;
    }

    private String getSunetIdFromCookie(final HttpServletRequest request) {
        if (this.cryptor == null) {
            throw new RuntimeException("cryptor is null");
        }
        Cookie sunetIdCookie = null;
        Cookie securityCookie = null;
        Cookie dateCookie = null;
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            // FIXME: remove this check after fixing tests:
            if (null != cookies[i]) {
                String name = cookies[i].getName();
                if (LanewebConstants.USER_COOKIE_NAME.equals(name)) {
                    sunetIdCookie = cookies[i];
                } else if (LanewebConstants.SECURITY_COOKIE_NAME.equals(name)) {
                    securityCookie = cookies[i];
                } else if (LanewebConstants.DATE_COOKIE_NAME.equals(name)) {
                    dateCookie = cookies[i];
                }
            }
        }
        if ((null == sunetIdCookie) || (null == securityCookie) || (dateCookie == null)) {
            return null;
        }
        String decryptedDate;
        String decryptedSunetId;
        String decryptedSecurity;
        try {
            decryptedSecurity = this.cryptor.decrypt(securityCookie.getValue());
            decryptedSunetId = this.cryptor.decrypt(sunetIdCookie.getValue());
            decryptedDate = this.cryptor.decrypt(dateCookie.getValue());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return null;
        }
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

    private void setLdapData(final User user) {
        if (null != user.getSunetId() && null == user.getDisplayName()) {
            Subject subject = this.subjectSource.getSubject();
            if (null != subject) {
                Subject.doAs(subject, new PrivilegedAction<User>() {

                    public User run() {
                        UserDao.this.ldapTemplate.search("", "susunetid=" + user.getSunetId(), new AttributesMapper() {

                            public Object mapFromAttributes(final Attributes attributes) throws NamingException {
                                user.setDisplayName((String) attributes.get("displayname").get());
                                user.setAffiliation((String) attributes.get("suaffiliation").get());
                                user.setUnivId((String) attributes.get("suunivid").get());
                                return user;
                            }
                        });
                        return user;
                    }
                });
            }
        }
    }

    private void setProxyLinks(final User user, final HttpServletRequest request) {
        String proxyLinks = request.getParameter(LanewebConstants.PROXY_LINKS);
        if (null != proxyLinks) {
            user.setProxyLinks(new Boolean(proxyLinks));
        }
    }

    private void setSunetId(final User user, final HttpServletRequest request) {
        if (user.getSunetId() == null) {
            String sunetId = request.getRemoteUser();
            if (sunetId == null) {
                sunetId = request.getHeader("x-webauth-user");
                if ("(null)".equals(sunetId)) {
                    sunetId = null;
                }
            }
            if (sunetId == null) {
                sunetId = getSunetIdFromCookie(request);
            }
            user.setSunetId(sunetId);
        }
    }

    private void setTicket(final User user, final HttpServletRequest request) {
        Ticket ticket = user.getTicket();
        if (null != user.getSunetId() && (null == ticket || !ticket.isValid())) {
            user.setTicket(new Ticket(user.getSunetId(), this.ezproxyKey));
        }
    }

    private void setUserTrackingAffiliation(final User user, final HttpServletRequest request) {
        if (user.getTrackingAffiliation() == null) {
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
            IPGroup iPGroup = IPGroup.getGroupForIP(ip);
            user.setTrackingAffiliation(iPGroup);
            if (IPGroup.ERR.equals(iPGroup)) {
                this.logger.error("error parsing ip for IPGroup: " + ip);
            }
        }
    }
}
