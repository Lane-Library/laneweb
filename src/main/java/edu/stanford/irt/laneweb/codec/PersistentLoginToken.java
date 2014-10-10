package edu.stanford.irt.laneweb.codec;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginToken {

    private static final int TWO_WEEKS = 1000 * 60 * 60 * 24 * 7 * 2;

    private long dateValue;

    private String encryptedValue;

    private int userAgentHash;
    
    private User user;

    public PersistentLoginToken(final User user, final long dateValue, final int userAgentHash, final String encryptedValue) {
        if (null == user) {
            throw new LanewebException("null user");
        }
        if (null == encryptedValue) {
            throw new LanewebException("null encryptedValue");
        }
        this.user = user;
        this.dateValue = dateValue;
        this.userAgentHash = userAgentHash;
        this.encryptedValue = encryptedValue;
    }

    public String getEncryptedValue() {
        return this.encryptedValue;
    }

    public User getUser() {
        return this.user;
    }

    public boolean isValidFor(final long dateValue, final int userAgentHash) {
        return userAgentHash == this.userAgentHash && this.dateValue + TWO_WEEKS > dateValue;
    }
}
