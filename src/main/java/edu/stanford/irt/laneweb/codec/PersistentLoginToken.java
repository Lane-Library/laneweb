package edu.stanford.irt.laneweb.codec;

import java.time.Duration;
import java.util.Objects;

import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginToken {

    private static final long FOUR_WEEKS = Duration.ofDays(28).toMillis();

    private long dateValue;

    private String encryptedValue;

    private User user;

    private int userAgentHash;

    public PersistentLoginToken(final User user, final long dateValue, final int userAgentHash,
            final String encryptedValue) {
        this.user = Objects.requireNonNull(user, "null user");
        this.dateValue = dateValue;
        this.userAgentHash = userAgentHash;
        this.encryptedValue = Objects.requireNonNull(encryptedValue, "null encryptedValue");
    }

    public String getEncryptedValue() {
        return this.encryptedValue;
    }

    public User getUser() {
        return this.user;
    }

    public boolean isValidFor(final long dateValue, final int userAgentHash) {
        return userAgentHash == this.userAgentHash && this.dateValue + FOUR_WEEKS > dateValue;
    }
}
