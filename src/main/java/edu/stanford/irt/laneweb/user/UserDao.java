package edu.stanford.irt.laneweb.user;

import java.security.PrivilegedAction;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.proxy.Ticket;

public class UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class);

    private String ezproxyKey;

    private LdapTemplate ldapTemplate;

    private SubjectSource subjectSource;

    public void getUserData(final User user, final HttpServletRequest request) {
        setTicket(user, request);
        setLdapData(user, request);
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

    private void setLdapData(final User user, final ServletRequest request) {
        if (null != request.getAttribute(LanewebObjectModel.SUNETID) && null == user.getName()) {
            Subject subject = this.subjectSource.getSubject();
            if (null != subject) {
                try {
                Subject.doAs(subject, new PrivilegedAction<User>() {

                    public User run() {
                        UserDao.this.ldapTemplate.search("", "susunetid=" + request.getAttribute(LanewebObjectModel.SUNETID), new AttributesMapper() {

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
                } catch (AuthenticationException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    private void setTicket(final User user, final HttpServletRequest request) {
        Ticket ticket = user.getTicket();
        if (null != request.getAttribute(LanewebObjectModel.SUNETID) && (null == ticket || !ticket.isValid())) {
            user.setTicket(new Ticket((String) request.getAttribute(LanewebObjectModel.SUNETID), this.ezproxyKey));
        }
    }
}
