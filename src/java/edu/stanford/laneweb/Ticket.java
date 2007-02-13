/**
 * 
 */
package edu.stanford.laneweb;

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

	public Ticket(final String ezproxyKey, final String user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (ezproxyKey == null) {
			throw new IllegalArgumentException("null ezproxyKey");
		}
		if (user == null) {
			throw new IllegalArgumentException("null user");
		}
		Date now = new Date();
		String packet = "$u" + ((int) (now.getTime() / 1000));
		this.stringValue = URLEncoder.encode(getKeyedDigest(ezproxyKey + user
				+ packet)
				+ packet, "UTF8");
	}

	private String getKeyedDigest(String buffer) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		StringBuffer sb = new StringBuffer();
		MessageDigest d = MessageDigest.getInstance("MD5");
		byte[] b = d.digest(buffer.getBytes("UTF8"));
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toHexString((b[i] & 0xf0) >> 4)
					+ Integer.toHexString(b[i] & 0x0f));
		}
		return sb.toString();
	}
	
	public String toString() {
		return this.stringValue;
	}
	
}
