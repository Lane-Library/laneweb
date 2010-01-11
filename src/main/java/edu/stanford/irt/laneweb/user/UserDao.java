package edu.stanford.irt.laneweb.user;

import java.security.PrivilegedAction;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.Cryptor;
import edu.stanford.irt.laneweb.LanewebConstants;

public class UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class);

    private Cryptor cryptor;

    private String ezproxyKey;

    private LdapTemplate ldapTemplate;

    private SubjectSource subjectSource;

    public User createOrUpdateUser(final HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute(LanewebConstants.USER);
        if (null == user) {
            user = new User();
            session.setAttribute(LanewebConstants.USER, user);
        }
        getUserData(user, request);
        return user;
    }

    public void getUserData(final User user, final HttpServletRequest request) {
        setIpGroup(user, request);
        setSunetId(user, request);
        setTicket(user, request);
        setProxyLinks(user, request);
        setLdapData(user);
        setEmrId(user, request);
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

    private String getRemoteAddr(final HttpServletRequest request) {
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
        return ip;
    }

    private String getSunetIdFromCookie(final HttpServletRequest request) {
        if (this.cryptor == null) {
            throw new RuntimeException("cryptor is null");
        }
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }
        Cookie laneCookie = null;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (LanewebConstants.LANE_COOKIE_NAME.equals(name)) {
                laneCookie = cookie;
                break;
            }
        }
        if (laneCookie != null) {
            try {
                PersistentLoginToken token = this.cryptor.restoreLoginToken(laneCookie.getValue());
                int userAgentHash = request.getHeader("User-Agent").hashCode();
                if (token.isValidFor(System.currentTimeMillis(), userAgentHash)) {
                    return token.getSunetId();
                }
            } catch (IllegalArgumentException e) {
                LOGGER.error("Cookie cannot be decrypted, it was maybe modified by user. IP --> "
                        .concat(getRemoteAddr(request)));
            }
        }
        return null;
    }

    private void setEmrId(final User user, final HttpServletRequest request) {
        if (null == user.getEmrId() || null != request.getParameter(User.EMRID)) {
            user.setEmrId(request.getParameter(User.EMRID));
        }
    }

    private void setIpGroup(final User user, final HttpServletRequest request) {
        if (user.getIPGroup() == null) {
            String ip = getRemoteAddr(request);
            IPGroup iPGroup = IPGroup.getGroupForIP(ip);
            user.setIPGroup(iPGroup);
            if (IPGroup.ERR.equals(iPGroup)) {
                LOGGER.error("error parsing ip for IPGroup: " + ip);
            }
        }
    }

    private void setLdapData(final User user) {
        if (null != user.getSunetId() && null == user.getName()) {
            Subject subject = this.subjectSource.getSubject();
            if (null != subject) {
                Subject.doAs(subject, new PrivilegedAction<User>() {

                    public User run() {
                        UserDao.this.ldapTemplate.search("", "susunetid=" + user.getSunetId(), new AttributesMapper() {

                            public Object mapFromAttributes(final Attributes attributes) throws NamingException {
                                Attribute currentAttribute = attributes.get("displayName");
                                if (null != currentAttribute) {
                                    user.setName((String) currentAttribute.get());
                                }
                                currentAttribute = attributes.get("suaffiliation");
                                if (null != currentAttribute) {
                                    user.setAffiliation((String) currentAttribute.get());
                                }
                                currentAttribute = attributes.get("suunivid");
                                if (null != currentAttribute) {
                                    user.setUnivId((String) currentAttribute.get());
                                }
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
            user.setProxyLinks(Boolean.valueOf(proxyLinks));
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
}
