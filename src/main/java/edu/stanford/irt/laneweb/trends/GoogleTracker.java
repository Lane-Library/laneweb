package edu.stanford.irt.laneweb.trends;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * <pre>
 * based on: http://code.google.com/intl/en/apis/analytics/docs/mobile/mobileWebsites.html#jsp 
 * useful reference: http://code.google.com/p/gaforflash/
 * </pre>
 * 
 * @author ryanmax
 */
// Copied from the trends-tracker project in order to remove log4j dependency
public class GoogleTracker {

    private static final String GA_GIF_LOCATION = "http://www.google-analytics.com/__utm.gif";

    // Tracker version.
    private static final String GA_VERSION = "4.4sj";

    private String domainName;

    private String googleAccount;

    private String localHostIp = null;

    private Logger logger;

    private String referer;

    private String userAgent;

    private String utmaCookie = null;

    private String visitorId = null;

    public GoogleTracker(final Logger logger) {
        this.logger = logger;
    }

    public void setDomainName(final String domainName) {
        this.domainName = domainName;
    }

    public void setGoogleAccount(final String googleAccount) {
        this.googleAccount = googleAccount;
    }

    public void setReferer(final String referer) {
        this.referer = referer;
    }

    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    public void trackEvent(final String path, final String category, final String action, final String label,
            final int value) {
        String utmUrl;
        try {
            utmUrl = GA_GIF_LOCATION + "?" + "utmwv=" + GA_VERSION + "&utmn=" + getRandomNumber() + "&utmhn="
                    + encode(this.domainName) + "&utmt=" + "event" + "&utme=" + "5(" + encode(category) + "*"
                    + encode(action) + "*" + encode(label) + ")(" + value + ")" + "&utmr=" + encode(this.referer)
                    + "&utmp=" + encode(path) + "&utmac=" + this.googleAccount + "&utmcc=__utma" + getUtmaCookie()
                    + "&utmvid=" + getVisitorId() + "&utmip=" + anonymizeIP(getLocalHostIP());
            sendRequestToGoogleAnalytics(utmUrl);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new LanewebException(e);
        }
        this.logger.info(new StringBuilder(path).append("/").append(category).append("/").append(action).append("/")
                .append(label).append("/").append(value).toString());
    }

    /**
     * The last octect of the IP address is removed to anonymize the user.
     * 
     * @param remoteAddress
     * @return
     */
    private String anonymizeIP(final String remoteAddress) {
        String empty = "";
        if (isEmpty(remoteAddress)) {
            return empty;
        }
        // Capture the first three octects of the IP address and replace the forth
        // with 0, e.g. 124.455.3.123 becomes 124.455.3.0
        String regex = "^([^.]+\\.[^.]+\\.[^.]+\\.).*";
        Pattern getFirstBitOfIPAddress = Pattern.compile(regex);
        Matcher m = getFirstBitOfIPAddress.matcher(remoteAddress);
        if (m.matches()) {
            return m.group(1) + "0";
        }
        return empty;
    }

    /**
     * @param string
     * @return URL encoded string, with %20 instead of plus for spaces
     * @throws UnsupportedEncodingException
     */
    private String encode(final String string) throws IOException {
        String encodedString = string;
        if (isEmpty(encodedString)) {
            encodedString = "";
        } else {
            encodedString = URLEncoder.encode((encodedString), "UTF-8");
            encodedString = encodedString.replaceAll("\\+", "%20");
        }
        return encodedString;
    }

    /**
     * The __utma cookie is used to determine unique visitors to your site and it is updated with each page view.
     * Additionally, this cookie is provided with a unique ID that Google Analytics uses to ensure both the validity and
     * accessibility of the cookie as an extra security measure. The __utma cookie is a persistent cookie keeping track
     * of the number of times a visitor has been to the site pertaining to the cookie, when their first visit was, and
     * when their last visit occurred. Values are hashed:
     * __utma=<domainHash>.<sessionId>.<firstTime>.<lastTime>.<currentTime>.<sessionCount>. Here we give dummy values
     * for all but domain and visitorId
     * 
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private String generateUtmaCookie() throws NoSuchAlgorithmException, IOException {
        return "%3D" + hash(this.domainName) + '.' + hash(getVisitorId()) + ".999.999.999.1%3B";
    }

    /**
     * @return a random visitorId
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private String generateVisitorId() throws NoSuchAlgorithmException, IOException {
        String message = this.userAgent + getRandomNumber() + UUID.randomUUID().toString();
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(message.getBytes("UTF-8"), 0, message.length());
        byte[] sum = m.digest();
        BigInteger messageAsNumber = new BigInteger(1, sum);
        String md5String = messageAsNumber.toString(16);
        // Pad to make sure id is 32 characters long.
        while (md5String.length() < 32) {
            md5String = "0" + md5String;
        }
        return "0x" + md5String.substring(0, 16);
    }

    private String getLocalHostIP() throws IOException {
        if (null != this.localHostIp) {
            return this.localHostIp;
        }
        InetAddress inetAddress = InetAddress.getLocalHost();
        this.localHostIp = inetAddress.getHostAddress();
        return this.localHostIp;
    }

    /**
     * @return a random number string.
     */
    private String getRandomNumber() {
        return Integer.toString((int) (Math.random() * 0x7fffffff));
    }

    private String getUtmaCookie() throws NoSuchAlgorithmException, IOException {
        if (null == this.utmaCookie) {
            this.utmaCookie = generateUtmaCookie();
        }
        return this.utmaCookie;
    }

    private String getVisitorId() throws NoSuchAlgorithmException, IOException {
        if (null == this.visitorId) {
            this.visitorId = generateVisitorId();
        }
        return this.visitorId;
    }

    /**
     * based on http://code.google.com/p/gaforflash/source/browse/trunk/src/com/google/analytics/core/generateHash.as
     * 
     * @param input
     * @return hash value of input string
     */
    private int hash(final String input) {
        int hash = 1; // hash buffer
        int leftMost7 = 0; // left-most 7 bits
        int pos; // character position in string
        int current; // current character in string
        // if input is null or empty, hash value is 1
        if (input != null && !input.isEmpty()) {
            hash = 0;
            // hash function
            for (pos = input.length() - 1; pos >= 0; pos--) {
                current = input.charAt(pos);
                hash = ((hash << 6) & 0xfffffff) + current + (current << 14);
                leftMost7 = hash & 0xfe00000;
                if (leftMost7 != 0) {
                    hash ^= leftMost7 >> 21;
                }
            }
        }
        return hash;
    }

    /**
     * A string is empty in our terms, if it is null, empty or a dash.
     * 
     * @param input
     * @return boolean
     */
    private boolean isEmpty(final String input) {
        return input == null || "-".equals(input) || "".equals(input);
    }

    /**
     * Make a tracking request to Google Analytics from this server.
     * 
     * @param utmUrl
     * @throws IOException
     */
    private void sendRequestToGoogleAnalytics(final String utmUrl) throws IOException {
        URL url = new URL(utmUrl);
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        connection.addRequestProperty("User-Agent", this.userAgent);
        connection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        connection.getContent();
    }
}
