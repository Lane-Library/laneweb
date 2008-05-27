package edu.stanford.irt.laneweb;

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
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.Request;

import edu.stanford.irt.directory.LDAPPerson;

public class WebDashLoginImpl extends AbstractLogEnabled implements
        WebDashLogin, ThreadSafe, Parameterizable, Initializable {

    private String loginUrl;

    private String registrationUrl;

    private String groupName;

    private Mac mac;

    public void initialize() throws Exception {
        Context context = new InitialContext();
        String groupKey = (String) context.lookup("java:comp/env/webdash-key");
        SecretKey key = new SecretKeySpec(groupKey.getBytes(), "HmacSHA1");
        this.mac = Mac.getInstance(key.getAlgorithm());
        this.mac.init(key);
    }

    public String getEncodedUrl(final LDAPPerson ldapPerson,
            final Request request) throws UnsupportedEncodingException,
            InvalidKeyException, NoSuchAlgorithmException {

        String error = validation(ldapPerson, request);
        if (error != null) {
            return "/error_webdash.html?error=".concat(error);
        }
        String mail = URLEncoder.encode(ldapPerson.getUId().concat(
                "@stanford.edu"), "UTF-8");
        String fullName = URLEncoder.encode(ldapPerson.getDisplayName(),
                "UTF-8");
        String userId = URLEncoder.encode(ldapPerson.getUId(), "UTF-8");
        String affiliation = getSubGroup(ldapPerson);
        String nonce = request.getParameter("nonce");
        StringBuffer parameters = new StringBuffer();
        parameters.append("email=");
        parameters.append(mail);
        parameters.append("&fullname=".concat(fullName.replace("+", "%20")));
        parameters.append("&nonce=");
        if (nonce != null) {
            parameters.append(nonce);
        }
        parameters.append("&subgroup=".concat(affiliation));
        parameters.append("&system_short_name=".concat(this.groupName));
        parameters
                .append("&system_user_id=".concat(userId.replace("+", "%20")));
        String token = getToken(parameters.toString());
        if (request.getParameter("system_user_id") != null) {
            return this.loginUrl.concat(parameters.toString())
                    .concat("&token=").concat(token);
        }
        return this.registrationUrl.concat(parameters.toString()).concat(
                "&token=").concat(token);
    }

    private String getToken(final String string)
            throws UnsupportedEncodingException {
        String newString = string.replace("+", "%20");
        byte[] utf8 = newString.getBytes("UTF8");
        byte[] b = this.mac.doFinal(utf8);
        StringBuffer sb = new StringBuffer();
        for (byte element : b) {
            sb.append(Integer.toHexString((element & 0xf0) >> 4)
                    + Integer.toHexString(element & 0x0f));
        }
        return sb.toString();

    }

    private String validation(final LDAPPerson ldapPerson, final Request request) {
        if (ldapPerson == null) {
            return "ldapPerson";
        }
        if ((request.getParameter("nonce") == null)
                || (request.getParameter("nonce").length() == 0)) {
            return "nonce";
        }
        if ((ldapPerson.getDisplayName() == null)
                || (ldapPerson.getDisplayName().length() == 0)) {
            return "fullName";
        }
        if ((ldapPerson.getAffilation() == null)
                || (ldapPerson.getAffilation().length == 0)) {
            return "affilation";
        }
        if ((ldapPerson.getUId() == null)
                || (ldapPerson.getUId().length() == 0)) {
            return "userId";
        }
        return null;

    }

    private String getSubGroup(final LDAPPerson ldapPerson)
            throws UnsupportedEncodingException {
        // value coming from LDAP and afflialtion may have multiple value e.i
        // stanford:staff
        String result = null;
        String[] affiliations = ldapPerson.getAffilation();
        if (affiliations.length > 0) {
            String[] affiliation = affiliations[0].split(":");
            if (affiliation.length > 0) {
                result = URLEncoder.encode(affiliation[1], "UTF-8");
            } else {
                result = URLEncoder.encode(affiliation[0], "UTF-8");
            }
        } else {
            throw new RuntimeException("Ldap person : ".concat(
                    ldapPerson.getDisplayName()).concat(
                    "  don't have a affiliation"));
        }
        return result;
    }

    public void parameterize(final Parameters param) throws ParameterException {
        this.registrationUrl = param.getParameter("webdashRegistrationURL");
        this.loginUrl = param.getParameter("webdashLoginURL");
        this.groupName = param.getParameter("groupName");

    }

}
