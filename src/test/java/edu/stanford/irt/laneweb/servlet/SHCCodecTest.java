/**
 * 
 */
package edu.stanford.irt.laneweb.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ryanmax
 */
public class SHCCodecTest {

    String ciphertext = "sbDXoSTqnSX0wuCP6JtvAAh9Ll9CWPdMR8Y5EcQ8WXw=";

    SHCCodec codec;

    String plaintext = "plain piece of text";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.codec = new SHCCodec();
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.servlet.SHCCodec#decrypt(java.lang.String)}
     * .
     */
    @Test
    public final void testDecrypt() {
        assertEquals(this.plaintext, this.codec.decrypt(this.ciphertext));
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.servlet.SHCCodec#encrypt(java.lang.String)}
     * .
     */
    @Test
    public final void testEncrypt() {
        assertEquals(this.ciphertext, this.codec.encrypt(this.plaintext));
    }

    @Test
    public final void testRoundtrip() {
        assertEquals(this.plaintext, this.codec.decrypt(this.codec.encrypt(this.plaintext)));
    }
}
