package edu.stanford.irt.laneweb.servlet;

import java.io.UnsupportedEncodingException;
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
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHCCodec {

    private static final String INITIAL_VECTOR = "KL75*$kjodh89ds4";

    private static final int ITERATIONS = 2;

    private static final int KEY_LENGTH = 128;

    private static final String PASSWORD = "Stanford@2012";

    private static final String SALT = "Stanford1!";

    private Cipher cipher;

    private byte[] initialVectorBytes;

    private Logger log = LoggerFactory.getLogger(SHCCodec.class);

    private byte[] saltBytes;

    private SecretKey secretKey;

    public SHCCodec() {
        /*
         * Derive the key, given password, salt, iterations, and keylength, as specified here:
         * https://irt-bugs.stanford.edu/default.asp?68825
         */
        try {
            this.saltBytes = SALT.getBytes("ASCII");
            /*
             * SHC using .NET PasswordDeriveBytes Class for key generation
             * http://msdn.microsoft.com/en-us/library/zb9zth5a%28v=vs.110%29.aspx
             * "This class uses an extension of the PBKDF1 algorithm defined in the PKCS#5 v2.0 standard to derive bytes suitable for use as key material from a password. The standard is documented in IETF RRC 2898."
             * mimic .NET PasswordDeriveBytes using PKCS5S1ParametersGenerator
             * http://bouncy-castle.1462172.n4.nabble.com/NET-PasswordDeriveBytes-td1462616.html
             */
            PKCS5S1ParametersGenerator generator = new PKCS5S1ParametersGenerator(new SHA1Digest());
            generator.init(PASSWORD.getBytes("ASCII"), this.saltBytes, ITERATIONS);
            byte[] keyBytes = ((KeyParameter) generator.generateDerivedParameters(KEY_LENGTH)).getKey();
            this.secretKey = new SecretKeySpec(keyBytes, "AES");
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.initialVectorBytes = INITIAL_VECTOR.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String decrypt(final String ciphertext) {
        byte[] ciphertextBytes = Base64.decodeBase64(ciphertext);
        String plaintext = null;
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey, new IvParameterSpec(this.initialVectorBytes));
            plaintext = new String(this.cipher.doFinal(ciphertextBytes), "UTF-8");
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (IllegalBlockSizeException e) {
            this.log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            this.log.error(e.getMessage(), e);
        }
        return plaintext;
    }

    public String encrypt(final String plaintext) {
        byte[] ciphertext = null;
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, new IvParameterSpec(this.initialVectorBytes));
            ciphertext = this.cipher.doFinal(plaintext.getBytes("UTF-8"));
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (IllegalBlockSizeException e) {
            this.log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            this.log.error(e.getMessage(), e);
        }
        return Base64.encodeBase64String(ciphertext);
    }
}
