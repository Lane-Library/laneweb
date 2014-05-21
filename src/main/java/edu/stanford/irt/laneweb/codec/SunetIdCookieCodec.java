package edu.stanford.irt.laneweb.codec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import edu.stanford.irt.laneweb.LanewebException;

//TODO: remove code duplicated with SHCCodec
public class SunetIdCookieCodec {

    public static final String LANE_COOKIE_NAME = "user";

    private static final String COOKIE_VALUE_SEPARATOR = "%";

    private Cipher cipher;

    private SecretKey desKey;

    public SunetIdCookieCodec(final String key) {
        try {
            // latest version of commons-codec (1.6) does not pad with 0 bytes
            // to 16, so do that here:
            byte[] src = Base64.decodeBase64(key.getBytes(StandardCharsets.UTF_8));
            byte[] dst = new byte[16];
            System.arraycopy(src, 0, dst, 0, src.length);
            this.desKey = new SecretKeySpec(dst, "AES");
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new LanewebException(e);
        } catch (NoSuchPaddingException e) {
            throw new LanewebException(e);
        }
    }

    public PersistentLoginToken createLoginToken(final String sunetId, final int userAgentHash) {
        long now = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        builder.append(sunetId);
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(now);
        builder.append(COOKIE_VALUE_SEPARATOR);
        builder.append(userAgentHash);
        String encryptedValue = encrypt(builder.toString());
        return new PersistentLoginToken(sunetId, now, userAgentHash, encryptedValue);
    }

    public PersistentLoginToken restoreLoginToken(final String encryptedValue) {
        if (encryptedValue == null) {
            throw new LanewebException("null encryptedValue");
        }
        String decrypted = decrypt(encryptedValue);
        String[] values = decrypted.split(COOKIE_VALUE_SEPARATOR);
        if (values.length != 3) {
            throw new LanewebException("invalid encryptedValue");
        }
        try {
            return new PersistentLoginToken(values[0], Long.parseLong(values[1]), Integer.parseInt(values[2]), encryptedValue);
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
        } catch (InvalidKeyException e) {
            throw new LanewebException(e);
        } catch (IllegalBlockSizeException e) {
            throw new LanewebException(e);
        } catch (BadPaddingException e) {
            throw new LanewebException(e);
        }
    }

    private synchronized String encrypt(final String input) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.desKey);
            byte[] cleartext = input.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = this.cipher.doFinal(cleartext);
            return new String(Base64.encodeBase64(ciphertext), StandardCharsets.UTF_8);
        } catch (InvalidKeyException e) {
            throw new LanewebException(e);
        } catch (IllegalBlockSizeException e) {
            throw new LanewebException(e);
        } catch (BadPaddingException e) {
            throw new LanewebException(e);
        }
    }
}
