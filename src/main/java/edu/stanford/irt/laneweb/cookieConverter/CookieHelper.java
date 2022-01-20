package edu.stanford.irt.laneweb.cookieConverter;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import edu.stanford.irt.laneweb.LanewebException;

public abstract class CookieHelper {
    
    String key;
    
    protected static final char COOKIE_VALUE_SEPARATOR = '%';

    protected static final String COOKIE_VALUE_SEPARATOR_STRING = Character.toString(COOKIE_VALUE_SEPARATOR);

    protected static final int DATE = 3;

    protected static final int EMAIL = 2;

    protected static final int EXPECTED_VALUE_COUNT = 5;

    protected static final int ID = 0;

    protected static final int KEY_ARRAY_LENGTH = 16;

    protected static final int NAME = 1;

    protected static final int USER_AGENT_HASH = 4;
    
    protected Cipher cipher;

    protected SecretKey desKey;
    
    public void setCookieHelper(String key) {
        this.key = key;
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
    }
   
    

    protected synchronized String decrypt(final String codedInput) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.desKey);
            byte[] base = Base64.decodeBase64(codedInput.getBytes(StandardCharsets.UTF_8));
            byte[] cleartext = this.cipher.doFinal(base);
            return new String(cleartext, StandardCharsets.UTF_8);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new LanewebException(e);
        }
    }

    protected synchronized String encrypt(final String input) {
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
