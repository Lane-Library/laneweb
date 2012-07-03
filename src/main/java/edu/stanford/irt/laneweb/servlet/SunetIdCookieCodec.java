package edu.stanford.irt.laneweb.servlet;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SunetIdCookieCodec {

    public static final String LANE_COOKIE_NAME = "user";

    private static final String COOKIE_VALUE_SEPARATOR = "%";

    private static final String UTF8 = "UTF-8";

    private Cipher cipher;

    private SecretKey desKey;

    public SunetIdCookieCodec(final String key) {
        try {
            // latest version of commons-codec (1.6) does not pad with 0 bytes
            // to 16, so do that here:
            byte[] src = Base64.decodeBase64(key.getBytes(UTF8));
            byte[] dst = new byte[16];
            System.arraycopy(src, 0, dst, 0, src.length);
            this.desKey = new SecretKeySpec(dst, "AES");
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException(e);
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
        PersistentLoginToken token = new PersistentLoginToken(sunetId, now, userAgentHash, encryptedValue);
        return token;
    }

    public PersistentLoginToken restoreLoginToken(final String encryptedValue) {
        if (encryptedValue == null) {
            throw new IllegalArgumentException("null encryptedValue");
        }
        String decrypted = decrypt(encryptedValue);
        String[] values = decrypted.split(COOKIE_VALUE_SEPARATOR);
        if (values.length != 3) {
            throw new IllegalArgumentException("invalid encryptedValue");
        }
        try {
            return new PersistentLoginToken(values[0], Long.parseLong(values[1]), Integer.parseInt(values[2]), encryptedValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid encryptedValue", e);
        }
    }

    private synchronized String decrypt(final String codedInput) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.desKey);
            byte[] base = Base64.decodeBase64(codedInput.getBytes(UTF8));
            byte[] cleartext = this.cipher.doFinal(base);
            return new String(cleartext, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private synchronized String encrypt(final String input) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.desKey);
            byte[] cleartext = input.getBytes(UTF8);
            byte[] ciphertext = this.cipher.doFinal(cleartext);
            return new String(Base64.encodeBase64(ciphertext), UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalStateException(e);
        } catch (BadPaddingException e) {
            throw new IllegalStateException(e);
        }
    }
}
