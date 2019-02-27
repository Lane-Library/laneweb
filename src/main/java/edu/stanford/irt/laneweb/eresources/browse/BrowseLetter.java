package edu.stanford.irt.laneweb.eresources.browse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import edu.stanford.irt.laneweb.LanewebException;

public class BrowseLetter {

    private int count;

    private String letter;

    private String url;

    public BrowseLetter(final String requestUri, final String letter, final int count) {
        this.count = count;
        this.letter = letter;
        if ("1".equals(this.letter)) {
            this.letter = "#";
        }
        try {
            this.url = requestUri + "?a=" + URLEncoder.encode(this.letter, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException("won't happen", e);
        }
    }

    /**
     * @return the count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * @return the letter
     */
    public String getLetter() {
        return this.letter;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }
}
