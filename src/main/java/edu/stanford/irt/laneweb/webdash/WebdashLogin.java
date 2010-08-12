package edu.stanford.irt.laneweb.webdash;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class WebdashLogin {

    private static final String LOGIN_URL = "https://webda.sh/auth/auth_post?";

    private static final String REGISTRATION_URL = "https://webda.sh/auth/init_post?";

    private Mac mac;

    public String getWebdashURL(final String sunetId, final String name, final String affiliation, final String nonce,
            final String systemUserId) {
        if (null == this.mac) {
            throw new IllegalStateException("webdashKey not set");
        }
        if (null == sunetId) {
            throw new IllegalArgumentException("null sunetId");
        }
        if (null == name) {
            throw new IllegalArgumentException("null name");
        }
        if (null == affiliation) {
            throw new IllegalArgumentException("null affiliation");
        }
        if (null == nonce) {
            throw new IllegalArgumentException("null nonce");
        }
        StringBuilder result = new StringBuilder();
        try {
            String encodedId = encodeParameter(sunetId);
            String mail = encodeParameter(sunetId.concat("@stanford.edu"));
            String fullName = encodeParameter(name);
            String encodedAffiliation = getSubGroup(affiliation);
            result.append("email=").append(mail).append("&fullname=").append(fullName).append("&nonce=").append(nonce)
                    .append("&subgroup=").append(encodedAffiliation)
                    .append("&system_short_name=stanford-sunet&system_user_id=").append(encodedId);
            String token = getToken(result.toString());
            result.append("&token=").append(token);
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
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
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    private String encodeParameter(final String parameter) throws UnsupportedEncodingException {
        return URLEncoder.encode(parameter, "UTF-8").replaceAll("\\+", "%20");
    }

    private String getSubGroup(final String affiliation) throws UnsupportedEncodingException {
        String group = affiliation.substring(affiliation.indexOf(':') + 1);
        return URLEncoder.encode(group, "UTF-8");
    }

    private String getToken(final String string) throws UnsupportedEncodingException {
        byte[] utf8 = string.getBytes("UTF-8");
        byte[] bytes = this.mac.doFinal(utf8);
        StringBuilder sb = new StringBuilder();
        for (byte element : bytes) {
            sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
        }
        return sb.toString();
    }
}
