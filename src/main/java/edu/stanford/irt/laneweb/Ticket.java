/**
 * 
 */
package edu.stanford.irt.laneweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author ceyates
 * 
 */
public class Ticket {

    private String stringValue;

    public Ticket(final String user) {
        if (user == null) {
            throw new IllegalArgumentException("null user");
        }
        Date now = new Date();
        String packet = "$u" + ((int) (now.getTime() / 1000));
        try {
            this.stringValue = URLEncoder.encode(getKeyedDigest(LanewebConstants.EZPROXY_KEY + user + packet) + packet, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // won't happen
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            // won't happen
            throw new RuntimeException(e);
        }
    }

    private String getKeyedDigest(final String buffer) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuffer sb = new StringBuffer();
        MessageDigest d = MessageDigest.getInstance("MD5");
        byte[] b = d.digest(buffer.getBytes("UTF-8"));
        for (byte element : b) {
            sb.append(Integer.toHexString((element & 0xf0) >> 4) + Integer.toHexString(element & 0x0f));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

}
