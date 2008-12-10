package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;

public class Cryptor {

    private Cipher cipher;
    private SecretKey desKey;

    public String encrypt(final String input) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IOException, DecoderException {
        this.cipher.init(Cipher.ENCRYPT_MODE, this.desKey);
        byte[] cleartext = input.getBytes("UTF-8");
        byte[] ciphertext = this.cipher.doFinal(cleartext);
        return new String(Base64.encodeBase64(ciphertext), "UTF-8");
    }

    public String decrypt(final String codedInput) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, UnsupportedEncodingException, DecoderException {
        this.cipher.init(Cipher.DECRYPT_MODE, this.desKey);
        byte[] cleartext = this.cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(codedInput.getBytes("UTF-8")));
        return new String(cleartext, "UTF-8");
    }

    public void setKey(final String key) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        this.desKey = new SecretKeySpec(Base64.decodeBase64(key.getBytes("UTF-8")), "AES");
        this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }

}
