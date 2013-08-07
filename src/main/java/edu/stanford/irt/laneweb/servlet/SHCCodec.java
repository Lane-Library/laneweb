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
import org.slf4j.Logger;

public class SHCCodec {

    /**
     * prints the key for the given password (first argument) and salt (second
     * argument)
     * 
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(final String[] args) throws UnsupportedEncodingException {
        /*
         * Derive the key, given password, salt, iterations, and keylength, as
         * specified here: https://irt-bugs.stanford.edu/default.asp?68825
         */
        /*
         * SHC using .NET PasswordDeriveBytes Class for key generation
         * http://msdn.microsoft.com/en-us/library/zb9zth5a%28v=vs.110%29.aspx
         * "This class uses an extension of the PBKDF1 algorithm defined in the PKCS#5 v2.0 standard to derive bytes suitable for use as key material from a password. The standard is documented in IETF RRC 2898."
         * mimic .NET PasswordDeriveBytes using PKCS5S1ParametersGenerator
         * http:/
         * /bouncy-castle.1462172.n4.nabble.com/NET-PasswordDeriveBytes-td1462616
         * .html
         */
        // org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator
        // generator = new
        // org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator(new
        // org.bouncycastle.crypto.digests.SHA1Digest());
        // generator.init(args[0].getBytes("ASCII"), args[1].getBytes("ASCII"),
        // 2);
        // byte[] keyBytes = ((org.bouncycastle.crypto.params.KeyParameter)
        // generator.generateDerivedParameters(128)).getKey();
        // System.out.println(Base64.encodeBase64String(keyBytes));
    }

    private Cipher cipher;

    private byte[] initialVectorBytes;

    private final Logger log;

    private SecretKey secretKey;

    public SHCCodec(final String key, final String vector, final Logger log) {
        this.log = log;
        try {
            // latest version of commons-codec (1.6) does not pad with 0 bytes
            // to 16, so do that here:
            byte[] src = Base64.decodeBase64(key);
            byte[] dst = new byte[16];
            System.arraycopy(src, 0, dst, 0, src.length);
            this.secretKey = new SecretKeySpec(dst, "AES");
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.initialVectorBytes = vector.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized String decrypt(final String ciphertext) {
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

    public synchronized String encrypt(final String plaintext) {
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
