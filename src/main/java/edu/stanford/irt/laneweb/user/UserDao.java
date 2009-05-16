package edu.stanford.irt.laneweb.user;

import java.security.PrivilegedAction;
import java.util.Calendar;
import java.util.Date;

import javax.naming.NamingException;
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
        setIpGroup(user, request);
        setSunetId(user, request);
        setTicket(user, request);
        setProxyLinks(user, request);
        setLdapData(user);
        setEmrId(user, request);
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
            // FIXME: remove this check after fixing tests:
            if (null != cookie) {
                String name = cookie.getName();
                if (LanewebConstants.LANE_COOKIE_NAME.equals(name)) {
                    laneCookie = cookie;
                    break;
                }
            }
        }
        String sunetId = null;
        if (laneCookie != null) {
            String cookieValue;
            try {
                cookieValue = this.cryptor.decrypt(laneCookie.getValue());
            } catch (Exception e) {
                this.logger.error("Cookie cannot be decrypted, it was maybe modified by user. IP --> ".concat(getRemoteAddr(request)));
                return null;
            }
            String[] cookieValues = cookieValue.split(LanewebConstants.COOKIE_VALUE_SEPARATOR);
            if (cookieValues.length != 3) {
                return null;
            }
            String currentUserAgent = String.valueOf(request.getHeader("User-Agent").hashCode());
            String cookieUserAgent = cookieValues[2];
            if (!cookieUserAgent.equals(currentUserAgent)) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            Date date = null;
            try {
                date = new Date(Long.valueOf(cookieValues[1]).longValue());
            } catch (Exception e) {
                return null;
            }
            calendar.setTime(date);
            calendar.add(Calendar.WEEK_OF_YEAR, 2);
            if (calendar.before(Calendar.getInstance())) {
                return null;
            }
            sunetId = cookieValues[0];
            if (sunetId == null || "".equals(sunetId.trim())) {
                return null;
            }
        }
        return sunetId;
    }

    private void setEmrId(final User user, final HttpServletRequest request) {
      if(null == user.getEmrId() || null != request.getParameter(User.EMRID)){
        user.setEmrId(request.getParameter(User.EMRID));
      }
    }

    private void setIpGroup(final User user, final HttpServletRequest request) {
        if (user.getIPGroup() == null) {
            String ip = getRemoteAddr(request);
            IPGroup iPGroup = IPGroup.getGroupForIP(ip);
            user.setIPGroup(iPGroup);
            if (IPGroup.ERR.equals(iPGroup)) {
                this.logger.error("error parsing ip for IPGroup: " + ip);
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
                                user.setName((String) attributes.get("displayname").get());
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
