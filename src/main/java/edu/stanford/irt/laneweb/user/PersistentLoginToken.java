package edu.stanford.irt.laneweb.user;

public class PersistentLoginToken {

    private static final int TWO_WEEKS = 1000 * 60 * 60 * 24 * 7 * 2;

    private long dateValue;

    private String encryptedValue;

    private String sunetId;

    private int userAgentHash;

    public PersistentLoginToken(final String sunetId, final long dateValue, final int userAgentHash,
            final String encryptedValue) {
        if (null == sunetId) {
            throw new IllegalArgumentException("null sunetId");
        }
        if (null == encryptedValue) {
            throw new IllegalArgumentException("null encryptedValue");
        }
        this.sunetId = sunetId;
        this.dateValue = dateValue;
        this.userAgentHash = userAgentHash;
        this.encryptedValue = encryptedValue;
    }

    public String getEncryptedValue() {
        return this.encryptedValue;
    }

    public String getSunetId() {
        if (this.sunetId.length() == 0) {
            return null;
        }
        return this.sunetId;
    }

    public boolean isValidFor(final long dateValue, final int userAgentHash) {
        return userAgentHash == this.userAgentHash && this.dateValue + TWO_WEEKS > dateValue;
    }
}
