/**
 * 
 */
package edu.stanford.irt.laneweb.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author ceyates
 */
public class Ticket {

    private long creationTime;

    private String stringValue;

    public Ticket(final String user, final String ezyproxyKey) {
        if (null == user) {
            throw new IllegalArgumentException("null user");
        }
        if (null == ezyproxyKey) {
            throw new IllegalArgumentException("null ezproxyKey");
        }
        Date now = new Date();
        String packet = "$u" + ((int) (now.getTime() / 1000));
        try {
            this.stringValue = URLEncoder.encode(getKeyedDigest(ezyproxyKey + user + packet) + packet, "UTF-8");
            this.creationTime = System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            // won't happen
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            // won't happen
            throw new RuntimeException(e);
        }
    }

    public boolean isValid() {
        return System.currentTimeMillis() - this.creationTime < 1000 * 60;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    private String getKeyedDigest(final String buffer) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuffer sb = new StringBuffer();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = digest.digest(buffer.getBytes("UTF-8"));
        for (byte element : bytes) {
            sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
        }
        return sb.toString();
    }
}
