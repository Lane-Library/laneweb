package edu.stanford.irt.laneweb.trends;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * A factory for URLConnections that can be mocked in tests.
     */
    static class URLConnectionFactory {

        public URLConnection getConnection(final String url) throws IOException {
            return new URL(url).openConnection();
        }
    }

    private static final String GA_GIF_LOCATION = "http://www.google-analytics.com/__utm.gif";

    // Tracker version.
    private static final String GA_VERSION = "4.4sj";

    private static final Pattern PLUS_PATTERN = Pattern.compile("\\+");

    private static final int SIXTEEN = 16;

    private static final int THIRTY_TWO = 32;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private URLConnectionFactory connectionFactory;

    private String domainName;

    private String googleAccount;

    private String localHostIp;

    private Random rand = new Random();

    private String referer;

    private String userAgent;

    private String utmaCookie;

    private String visitorId;

    public GoogleTracker() {
        this(new URLConnectionFactory());
    }

    GoogleTracker(final URLConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
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
    private String encode(final String string) throws UnsupportedEncodingException {
        String encodedString = string;
        if (isEmpty(encodedString)) {
            encodedString = "";
        } else {
            encodedString = URLEncoder.encode(encodedString, UTF_8);
            encodedString = PLUS_PATTERN.matcher(encodedString).replaceAll("%20");
        }
        return encodedString;
    }

    /**
     * The __utma cookie is used to determine unique visitors to your site and it is updated with each page view.
     * Additionally, this cookie is provided with a unique ID that Google Analytics uses to ensure both the validity and
     * accessibility of the cookie as an extra security measure. The __utma cookie is a persistent cookie keeping track
     * of the number of times a visitor has been to the site pertaining to the cookie, when their first visit was, and
     * when their last visit occurred. Values are hashed: __utma=<domainHash>.<sessionId>.<firstTime>.<lastTime>.
     * <currentTime>.<sessionCount>. Here we give dummy values for all but domain and visitorId
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private String generateUtmaCookie() throws NoSuchAlgorithmException {
        return "%3D" + hash(this.domainName) + '.' + hash(getVisitorId()) + ".999.999.999.1%3B";
    }

    /**
     * @return a random visitorId
     * @throws NoSuchAlgorithmException
     */
    private String generateVisitorId() throws NoSuchAlgorithmException {
        String message = this.userAgent + getRandomNumber() + UUID.randomUUID();
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(message.getBytes(StandardCharsets.UTF_8), 0, message.length());
        byte[] sum = m.digest();
        BigInteger messageAsNumber = new BigInteger(1, sum);
        StringBuilder md5String = new StringBuilder(messageAsNumber.toString(SIXTEEN));
        // Pad to make sure id is 32 characters long.
        while (md5String.length() < THIRTY_TWO) {
            md5String.insert(0, "0");
        }
        md5String.setLength(SIXTEEN);
        return md5String.insert(0, "0x").toString();
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
        return Integer.toString(this.rand.nextInt(Integer.MAX_VALUE));
    }

    private String getUtmaCookie() throws NoSuchAlgorithmException {
        if (null == this.utmaCookie) {
            this.utmaCookie = generateUtmaCookie();
        }
        return this.utmaCookie;
    }

    private String getVisitorId() throws NoSuchAlgorithmException {
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
        // hash buffer
        int hash = 1;
        // left-most 7 bits
        int leftMost7;
        // character position in string
        int pos;
        // current character in string
        int current;
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
        URLConnection connection = this.connectionFactory.getConnection(utmUrl);
        connection.setUseCaches(false);
        connection.addRequestProperty("User-Agent", this.userAgent);
        connection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        connection.getContent();
    }
}
