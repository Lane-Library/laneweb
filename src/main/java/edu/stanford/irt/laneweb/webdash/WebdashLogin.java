package edu.stanford.irt.laneweb.webdash;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import edu.stanford.irt.laneweb.user.User;

public class WebdashLogin {

    private static final String ERROR_URL = "/webdashError.html";

    private static final String LOGIN_URL = "https://webda.sh/auth/auth_post?";

    private static final String REGISTRATION_URL = "https://webda.sh/auth/init_post?";

    private Logger logger = Logger.getLogger(WebdashLogin.class);

    private Mac mac;

    public String getWebdashURL(final User person, final String nonce, final String systemUserId) {
        if (null == this.mac) {
            throw new IllegalStateException("webdashKey not set");
        }
        if (null == person) {
            this.logger.error("null person");
            return ERROR_URL;
        }
        if (null == nonce) {
            this.logger.error("null nonce");
            return ERROR_URL;
        }
        String userId = encodeParameter(person.getSunetId());
        String mail = encodeParameter(userId.concat("@stanford.edu"));
        String fullName = encodeParameter(person.getName());
        String affiliation = getSubGroup(person);
        StringBuffer result = new StringBuffer();
        result.append("email=").append(mail).append("&fullname=").append(fullName).append("&nonce=").append(nonce).append("&subgroup=").append(affiliation)
                .append("&system_short_name=stanford-sunet&system_user_id=").append(userId);
        String token = getToken(result.toString());
        result.append("&token=").append(token);
        result.insert(0, systemUserId == null ? REGISTRATION_URL : LOGIN_URL);
        return result.toString();
    }

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

    private String encodeParameter(final String parameter) {
        if (null == parameter) {
            throw new IllegalArgumentException("null parameter");
        }
        try {
            return URLEncoder.encode(parameter, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not supported");
        }
    }

    private String getSubGroup(final User person) {
        String affiliation = person.getAffilation();
        if (null == affiliation) {
            throw new RuntimeException("no affiliations for " + person.getName());
        }
        return affiliation.substring(affiliation.indexOf(':') + 1);
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
            sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
        }
        return sb.toString();
    }
}
