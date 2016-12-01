package edu.stanford.irt.laneweb.proxy;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Ticket implements Serializable {

    private static final int ONE_SECOND = 1000;

    private static final int ONE_MINUTE = ONE_SECOND * 60;

    private static final long serialVersionUID = 1L;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private long creationTime;

    private String stringValue;

    public Ticket(final String userid, final String ezyproxyKey) {
        if (null == userid) {
            throw new IllegalArgumentException("null userid");
        }
        if (null == ezyproxyKey) {
            throw new IllegalArgumentException("null ezproxyKey");
        }
        Date now = new Date();
        String packet = "$u" + ((int) (now.getTime() / ONE_SECOND)) + "$e";
        try {
            this.stringValue = URLEncoder.encode(getKeyedDigest(ezyproxyKey + userid + packet) + packet, UTF_8);
            this.creationTime = System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isValid() {
        return System.currentTimeMillis() - this.creationTime < ONE_MINUTE;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    private String getKeyedDigest(final String buffer) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = digest.digest(buffer.getBytes(StandardCharsets.UTF_8));
        for (byte element : bytes) {
            sb.append(Integer.toHexString((element & 0xf0) >> 4)).append(Integer.toHexString(element & 0x0f));
        }
        return sb.toString();
    }
}
