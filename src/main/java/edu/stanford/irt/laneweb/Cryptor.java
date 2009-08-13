package edu.stanford.irt.laneweb;

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

public class Cryptor {

    private Cipher cipher;

    private SecretKey desKey;

    public String decrypt(final String codedInput) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.desKey);
            byte[] cleartext = this.cipher.doFinal(Base64.decodeBase64(codedInput.getBytes("UTF-8")));
            return new String(cleartext, "UTF-8");
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

    public String encrypt(final String input) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.desKey);
            byte[] cleartext = input.getBytes("UTF-8");
            byte[] ciphertext = this.cipher.doFinal(cleartext);
            return new String(Base64.encodeBase64(ciphertext), "UTF-8");
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

    public void setKey(final String key) {
        try {
            this.desKey = new SecretKeySpec(Base64.decodeBase64(key.getBytes("UTF-8")), "AES");
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
    }
}
