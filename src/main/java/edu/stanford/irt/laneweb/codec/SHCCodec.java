package edu.stanford.irt.laneweb.codec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class SHCCodec {

    private static final int KEY_ARRAY_LENGTH = 16;

    private static final Logger LOG = LoggerFactory.getLogger(SHCCodec.class);

    private Cipher cipher;

    private AlgorithmParameterSpec initialVector;

    private SecretKey secretKey;

    public SHCCodec(final String key, final String vector) {
        // pad with 0 bytes to 16:
        byte[] src = Base64.decodeBase64(key);
        byte[] dst = new byte[KEY_ARRAY_LENGTH];
        System.arraycopy(src, 0, dst, 0, src.length);
        this.secretKey = new SecretKeySpec(dst, "AES");
        this.initialVector = new IvParameterSpec(vector.getBytes(StandardCharsets.US_ASCII));
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new LanewebException(e);
        }
    }

    public String decrypt(final String ciphertext) {
        byte[] ciphertextBytes = Base64.decodeBase64(ciphertext);
        byte[] plaintext;
        synchronized (this.cipher) {
            initializeCipher(Cipher.DECRYPT_MODE);
            plaintext = doFinal(ciphertextBytes);
        }
        return new String(plaintext, StandardCharsets.UTF_8);
    }

    public synchronized String encrypt(final String plaintext) {
        byte[] plainTextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext;
        synchronized (this.cipher) {
            initializeCipher(Cipher.ENCRYPT_MODE);
            ciphertext = doFinal(plainTextBytes);
        }
        return Base64.encodeBase64String(ciphertext);
    }

    private byte[] doFinal(final byte[] input) {
        byte[] result = null;
        try {
            result = this.cipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    private void initializeCipher(final int mode) {
        try {
            this.cipher.init(mode, this.secretKey, this.initialVector);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new LanewebException(e);
        }
    }
}
