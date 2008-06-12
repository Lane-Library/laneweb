package edu.stanford.irt.laneweb.webdash;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;

import edu.stanford.irt.directory.LDAPPerson;

public class WebdashLoginImpl implements WebdashLogin, ThreadSafe,
        Parameterizable, Initializable {

    private String loginURL;

    private String registrationURL;

    private String groupName;

    private Mac mac;

    public void setWebdashKey(final String webdashKey) {
        if (null == webdashKey) {
            throw new IllegalArgumentException("null webdashKey");
        }
        SecretKey key = new SecretKeySpec(webdashKey.getBytes(), "HmacSHA1");
        try {
            this.mac = Mac.getInstance(key.getAlgorithm());
            this.mac.init(key);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLoginURL(final String loginURL) {
        if (null == loginURL) {
            throw new IllegalArgumentException("null loginURL");
        }
        this.loginURL = loginURL;
    }

    public void setRegistrationURL(final String registrationURL) {
        if (null == registrationURL) {
            throw new IllegalArgumentException("null registrationURL");
        }
        this.registrationURL = registrationURL;
    }

    public void setGroupName(final String groupName) {
        if (null == groupName) {
            throw new IllegalArgumentException("null groupName");
        }
        this.groupName = groupName;
    }

    public void initialize() throws Exception {
        Context context = new InitialContext();
        setWebdashKey((String) context.lookup("java:comp/env/webdash-key"));
    }

    public String getRegistrationURL(final LDAPPerson person, final String nonce) {
        return this.registrationURL.concat(getQueryString(person, nonce));
    }

    public String getLoginURL(final LDAPPerson person, final String nonce) {
        return this.loginURL.concat(getQueryString(person, nonce));
    }

    public String getQueryString(final LDAPPerson person, final String nonce) {
        if (null == person) {
            throw new IllegalArgumentException("null person");
        }
        if (null == nonce) {
            throw new IllegalArgumentException("null nonce");
        }
        if (null == this.groupName) {
            throw new IllegalStateException("null groupName");
        }
        if (null == this.mac) {
            throw new IllegalStateException("webdashKey not set");
        }
        if (null == this.registrationURL) {
            throw new IllegalStateException("null registrationURL");
        }
        if (null == this.loginURL) {
            throw new IllegalStateException("null logingURL");
        }
        String userId = encodeParameter(person.getUId());
        String mail = encodeParameter(userId.concat("@stanford.edu"));
        String fullName = encodeParameter(person.getDisplayName());
        String affiliation = getSubGroup(person);
        StringBuffer result = new StringBuffer();
        result.append("email=").append(mail).append("&fullname=").append(
                fullName).append("&nonce=").append(nonce).append("&subgroup=")
                .append(affiliation).append("&system_short_name=").append(
                        this.groupName).append("&system_user_id=").append(
                        userId);
        String token = getToken(result.toString());
        result.append("&token=").append(token);
        return result.toString();
    }

    private String encodeParameter(final String parameter) {
        if (null == parameter) {
            throw new IllegalArgumentException("null parameter");
        }
        try {
            return URLEncoder.encode(parameter, "UTF-8").replaceAll("\\+",
                    "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not supported");
        }
    }

    private String getToken(final String string) {
        byte[] utf8;
        try {
            utf8 = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not supported");
        }
        byte[] b = this.mac.doFinal(utf8);
        StringBuffer sb = new StringBuffer();
        for (byte element : b) {
            sb.append(Integer.toHexString((element & 0xf0) >> 4)
                    + Integer.toHexString(element & 0x0f));
        }
        return sb.toString();

    }

    private String getSubGroup(final LDAPPerson person) {
        // value coming from LDAP may have multiple values e.g.
        // stanford:staff
        String[] affiliations = person.getAffilation();
        if (0 == affiliations.length) {
            throw new RuntimeException("no affiliations for "
                    + person.getDisplayName());
        }
        String[] affiliation = affiliations[0].split(":");
        if (affiliation.length > 1) {
            return affiliation[1];
        }
        return affiliation[0];
    }

    public void parameterize(final Parameters param) {
        setRegistrationURL(param.getParameter("registration-url", null));
        setLoginURL(param.getParameter("login-url", null));
        setGroupName(param.getParameter("group-name", null));

    }

}
