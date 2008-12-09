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


    public String encrypt(String input) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
	    InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IOException, DecoderException {
	cipher.init(Cipher.ENCRYPT_MODE, desKey);
	byte[] cleartext = input.getBytes("UTF-8");
	byte[] ciphertext = cipher.doFinal(cleartext);
	return new String(Base64.encodeBase64(ciphertext), "UTF-8");
    }

    public String decrypt(String codedInput) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
	    NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, UnsupportedEncodingException, DecoderException {
	cipher.init(Cipher.DECRYPT_MODE, desKey);
	byte[] cleartext = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(codedInput.getBytes("UTF-8")));
	return new String(cleartext, "UTF-8");
    }

    public void setKey(String key) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
	this.desKey = new SecretKeySpec(Base64.decodeBase64(key.getBytes("UTF-8")), "AES");
	cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }

}
