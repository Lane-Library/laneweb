package edu.stanford.irt.laneweb.codec;

import edu.stanford.irt.laneweb.LanewebException;

public class PersistentLoginToken {

    private static final int TWO_WEEKS = 1000 * 60 * 60 * 24 * 7 * 2;

    private long dateValue;

    private String encryptedValue;

    private String userId;

    private int userAgentHash;

    public PersistentLoginToken(final String userId, final long dateValue, final int userAgentHash, final String encryptedValue) {
        if (null == userId) {
            throw new LanewebException("null userId");
        }
        if (null == encryptedValue) {
            throw new LanewebException("null encryptedValue");
        }
        this.userId = userId;
        this.dateValue = dateValue;
        this.userAgentHash = userAgentHash;
        this.encryptedValue = encryptedValue;
    }

    public String getEncryptedValue() {
        return this.encryptedValue;
    }

    public String getUserId() {
        if (this.userId.length() == 0) {
            return null;
        }
        return this.userId;
    }

    public boolean isValidFor(final long dateValue, final int userAgentHash) {
        return userAgentHash == this.userAgentHash && this.dateValue + TWO_WEEKS > dateValue;
    }
}
