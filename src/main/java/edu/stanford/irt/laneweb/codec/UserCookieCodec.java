package edu.stanford.irt.laneweb.codec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class UserCookieCodec {

    private static final char COOKIE_VALUE_SEPARATOR = '%';

    private static final String COOKIE_VALUE_SEPARATOR_STRING = Character.toString(COOKIE_VALUE_SEPARATOR);

    private static final int DATE = 3;

    private static final int EMAIL = 2;

    private static final int EXPECTED_VALUE_COUNT = 5;

    private static final int ID = 0;

    private static final int KEY_ARRAY_LENGTH = 16;

    private static final int NAME = 1;

    private static final int USER_AGENT_HASH = 4;

    private Cipher cipher;

    private Clock clock;

    private SecretKey desKey;

    public UserCookieCodec(final String key) {
        this(key, Clock.systemDefaultZone());
    }

    public UserCookieCodec(final String key, final Clock clock) {
        try {
            // latest version of commons-codec (1.6) does not pad with 0 bytes
            // to 16, so do that here:
            byte[] src = Base64.decodeBase64(key.getBytes(StandardCharsets.UTF_8));
            byte[] dst = new byte[KEY_ARRAY_LENGTH];
            System.arraycopy(src, 0, dst, 0, src.length > KEY_ARRAY_LENGTH ? KEY_ARRAY_LENGTH : src.length);
            this.desKey = new SecretKeySpec(dst, "AES");
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new LanewebException(e);
        }
        this.clock = clock;
    }

    public PersistentLoginToken createLoginToken(final User user, final int userAgentHash) {
        Objects.requireNonNull(user, "null user");
        long now = this.clock.millis();
        StringBuilder builder = new StringBuilder();
        builder.append(user.getId());
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(user.getName());
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(user.getEmail());
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(now);
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(userAgentHash);
        String encryptedValue = encrypt(builder.toString());
        return new PersistentLoginToken(user, now, userAgentHash, encryptedValue);
    }

    public PersistentLoginToken createLoginToken(final User user, final int userAgentHash, Long expirationDate) {
        Objects.requireNonNull(user, "null user");
        StringBuilder builder = new StringBuilder();
        builder.append(user.getId());
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(user.getName());
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(user.getEmail());
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(expirationDate);
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(userAgentHash);
        String encryptedValue = encrypt(builder.toString());
        return new PersistentLoginToken(user, expirationDate, userAgentHash, encryptedValue);
    }

    
    public PersistentLoginToken restoreLoginToken(final String encryptedValue, final String userIdHashKey) {
        Objects.requireNonNull(encryptedValue, "null encryptedValue");
        String decrypted = decrypt(encryptedValue);
        String[] values = decrypted.split(COOKIE_VALUE_SEPARATOR_STRING);
        if (values.length != EXPECTED_VALUE_COUNT) {
            throw new LanewebException("invalid encryptedValue");
        }
        try {
            return new PersistentLoginToken(new User(values[ID], values[NAME], values[EMAIL], userIdHashKey),
                    Long.parseLong(values[DATE]), Integer.parseInt(values[USER_AGENT_HASH]), encryptedValue);
        } catch (NumberFormatException e) {
            throw new LanewebException("invalid encryptedValue", e);
        }
    }

    private synchronized String decrypt(final String codedInput) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.desKey);
            byte[] base = Base64.decodeBase64(codedInput.getBytes(StandardCharsets.UTF_8));
            byte[] cleartext = this.cipher.doFinal(base);
            return new String(cleartext, StandardCharsets.UTF_8);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LanewebException(e);
        }
    }

    private synchronized String encrypt(final String input) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.desKey);
            byte[] cleartext = input.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = this.cipher.doFinal(cleartext);
            return new String(Base64.encodeBase64(ciphertext), StandardCharsets.UTF_8);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LanewebException(e);
        }
    }
}
