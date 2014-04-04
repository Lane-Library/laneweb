package edu.stanford.irt.laneweb.servlet;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;

public class SHCCodec {

    private Cipher cipher;

    private byte[] initialVectorBytes;

    private final Logger log;

    private SecretKey secretKey;

    public SHCCodec(final String key, final String vector, final Logger log) {
        this.log = log;
        // latest version of commons-codec (1.6) does not pad with 0 bytes
        // to 16, so do that here:
        byte[] src = Base64.decodeBase64(key);
        byte[] dst = new byte[16];
        System.arraycopy(src, 0, dst, 0, src.length);
        this.secretKey = new SecretKeySpec(dst, "AES");
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.initialVectorBytes = vector.getBytes("ASCII");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
    }

    public synchronized String decrypt(final String ciphertext) {
        byte[] ciphertextBytes = Base64.decodeBase64(ciphertext);
        String plaintext = null;
        initializeCipher(Cipher.DECRYPT_MODE);
        plaintext = new String(doFinal(ciphertextBytes), Charset.forName("UTF-8"));
        return plaintext;
    }

    public synchronized String encrypt(final String plaintext) {
        byte[] ciphertext = null;
        initializeCipher(Cipher.ENCRYPT_MODE);
        ciphertext = doFinal(plaintext.getBytes(Charset.forName("UTF-8")));
        return Base64.encodeBase64String(ciphertext);
    }

    private byte[] doFinal(final byte[] input) {
        byte[] result = null;
        try {
            result = this.cipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            this.log.error(e.getMessage(), e);
        }
        return result;
    }

    private void initializeCipher(int mode) {
        try {
            this.cipher.init(mode, this.secretKey, new IvParameterSpec(this.initialVectorBytes));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new LanewebException(e);
        }
    }
}
