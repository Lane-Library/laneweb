package edu.stanford.irt.laneweb.user;

import java.security.PrivilegedAction;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class);

    private String ezproxyKey;

    private LdapTemplate ldapTemplate;

    private SubjectSource subjectSource;

    public void getUserData(final User user, final HttpServletRequest request) {
        setIpGroup(user, request);
        setSunetId(user, request);
        setTicket(user, request);
        setProxyLinks(user, request);
        setLdapData(user);
        setEmrId(user, request);
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

    private void setEmrId(final User user, final HttpServletRequest request) {
        if (null == user.getEmrId() || null != request.getParameter(LanewebObjectModel.EMRID)) {
            user.setEmrId(request.getParameter(LanewebObjectModel.EMRID));
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
        user.setSunetId((String) request.getAttribute(LanewebObjectModel.SUNETID));
    }

    private void setTicket(final User user, final HttpServletRequest request) {
        Ticket ticket = user.getTicket();
        if (null != user.getSunetId() && (null == ticket || !ticket.isValid())) {
            user.setTicket(new Ticket(user.getSunetId(), this.ezproxyKey));
        }
    }
}
